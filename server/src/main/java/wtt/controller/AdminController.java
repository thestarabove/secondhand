package wtt.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wtt.pojo.Goods;
import wtt.pojo.Ratingform;
import wtt.pojo.User;
import wtt.service.*;
import wtt.utils.ComFoundException;
import wtt.utils.ResponseJsonStatus;
import wtt.vo.ResponseJsonMessage;
import wtt.vo.UserVo;

import java.util.List;

//管理员 ----注销账号，删除商品，审核卖家
@RestController
@RequestMapping("/secondhand/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RatingformService ratingformService;
    //登录验证
    @SaCheckLogin
    @GetMapping("/doLogin")
    public SaResult login(String code, String admin, HttpServletRequest request){
        JSONObject jsonObject= JSON.parseObject(admin);
        String codeSession= (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        System.out.println(" session中获取的验证码:  " + codeSession);
        if (code.equalsIgnoreCase(codeSession)){
            String account=jsonObject.getString("account");
            String pwd=jsonObject.getString("password");
           UserVo user1= loginService.loginUser(account,pwd);
            if (user1!=null){
                StpUtil.login(user1.getUserId());
                SaTokenInfo saTokenInfo =StpUtil.getTokenInfo();
                System.out.println(saTokenInfo);
                System.out.println(StpUtil.getPermissionList(saTokenInfo.loginId));
                System.out.println(StpUtil.getRoleList(saTokenInfo.loginId));
                // redisUtil.set("token"+saTokenInfo.tokenValue,saTokenInfo);
            }
        }else {
         return SaResult.error("验证码错误，请重试！");
        }
        return SaResult.ok("登录成功，会话id为"+StpUtil.getLoginId()+",Token为："+StpUtil.getTokenValue());
    }
    //注销登录
    @GetMapping("/loginOut")
    @SaCheckLogin
    public SaResult outLogin(){
        String loginId=null;
        if (StpUtil.isLogin()){
            loginId= (String) StpUtil.getLoginId();
            StpUtil.logout();
        }
        return SaResult.ok("会话ID为 " + loginId + " 的用户注销登录成功");
    }
    @GetMapping("/getAdmin")
    @SaCheckLogin
    @SaCheckRole("3")
    public ResponseJsonMessage getAdmin(){
        SaTokenInfo saTokenInfo =StpUtil.getTokenInfo();
        String userId= (String) saTokenInfo.getLoginId();
        User user=userService.getUser(Integer.valueOf(userId));
        UserVo userVo=new UserVo();
        userVo.setUserId(user.getUserId());
        userVo.setUserName(user.getUserName());
        userVo.setUserPhone(user.getUserPhone());
        userVo.setAccount(user.getAccount());
        userVo.setAddress(user.getAddress());
        userVo.setImg(user.getImg());
        userVo.setRole(user.getRole());
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),userVo);
    }
    //获取全部用户
    @GetMapping("/getAllUser")
    @SaCheckPermission("user-getAll")
    public ResponseJsonMessage getAllUser(){

        List<User> list=userService.getAllUser();
        if (list==null){
            throw new ComFoundException("获取用户失败！");
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),list);
    }
    //获取申请用户
    @GetMapping("/getUserBySeller")
    @SaCheckPermission("user-getAll")
    public ResponseJsonMessage getUserBySeller(){

        List<UserVo> list=userService.getUserBySeller();
        if (list==null){
            throw new ComFoundException("获取用户失败！");
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),list);
    }

    //注销用户
    @SaCheckPermission("user-delete")
    @GetMapping("/delete")
    public SaResult logDelete(@RequestParam("userId") Integer id){
        userService.deleteUser(id);
        return SaResult.ok();
    }
    //删除商品
    @SaCheckPermission("good-delete")
    @GetMapping("/deleteGood/{secondHandId}")
    public ResponseJsonMessage deleteGood(@PathVariable Integer secondHandId){
       Goods goods=goodsService.findGood(secondHandId);
       int flag;
       if (goods!=null){
           flag=goodsService.deleteGood(secondHandId);
       if (flag==0){
           throw new ComFoundException("删除失败！");
       }
       }else {
           throw new ComFoundException("查询商品失败！");
       }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),flag);
    }

    //审核卖家申请
    @GetMapping("/updateSeller")
    @SaCheckPermission("seller-add")
    public ResponseJsonMessage updateSeller(@RequestParam("sellerId") Integer sellerId){
      int f=userService.Seller(sellerId);
      if (f==0){
          throw new ComFoundException("审批失败！");
      }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),f);
    }
    @GetMapping("/failSeller")
    @SaCheckPermission("seller-add")
    public ResponseJsonMessage failSeller(@RequestParam("sellerId") Integer sellerId){
        int f=userService.failSeller(sellerId);
        if (f==0){
            throw new ComFoundException("审批失败！");
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),f);
    }

    //审核订单评价
    @GetMapping("/findRatingApp")
    @SaCheckRole("3")
    public ResponseJsonMessage findRating(){

        List<Ratingform> list= ratingformService.findFormByAdmin();
        if (list==null){
            return new ResponseJsonMessage(ResponseJsonStatus.FAILURE.getStatus(), ResponseJsonStatus.FAILURE.getMessage(),null);
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),list);
    }


    @GetMapping("/updateRatingApp")
    @SaCheckRole("3")
    public ResponseJsonMessage updateRating(@RequestParam("RatingId") Integer RatingId){
         int f=  ratingformService.updateRatingFormByAdmin(RatingId);
        if (f==0){
            throw new ComFoundException("审批失败！");
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),f);
    }


    @GetMapping("/failRatingApp")
    @SaCheckRole("3")
    public ResponseJsonMessage failRatingApp(@RequestParam("RatingId") Integer RatingId){
        int f=  ratingformService.failRatingFormByAdmin(RatingId);
        if (f==0){
            throw new ComFoundException("审批失败！");
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),f);
    }
}
