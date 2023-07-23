package wtt.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.basic.SaBasicUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.google.code.kaptcha.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wtt.Log.LogMethod;
import wtt.pojo.*;
import wtt.service.*;
import wtt.utils.*;
import wtt.vo.ResponseJsonMessage;
import wtt.vo.UserVo;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

@RestController
@RequestMapping("/secondhand/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LikesService likesService;
    @Autowired
    private RatingformService ratingformService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OptionalNull optionalNull;

    //--------------------------个人信息--------------------------
    @PostMapping ("/doLogin")
    @LogMethod
 public SaResult doLogin(@RequestBody @Param("data") Map<String,String> data){

        String account=data.get("account");
        String password=data.get("password");
        String code=data.get("verifyCode");
      //  System.out.println(data);
          String codeSession= (String) redisUtil.get("codeSession:code");
      //  System.out.println(codeSession+"------------codeSession");
        if (!code.equals( codeSession)){
            throw new ComFoundException("验证失败");
        }
       UserVo user = loginService.loginUser(account,password);
        //System.out.println(user);
        //这个方法会强制在浏览器弹出一个认证框
      //  SaBasicUtil.check("sa:123456");
     if (user!=null){
          StpUtil.login(user.getUserId());
          SaTokenInfo saTokenInfo =StpUtil.getTokenInfo();
         //System.out.println(StpUtil.getPermissionList());
         UserVo userVo=new UserVo();
         userVo.setUserId(user.getUserId());
         userVo.setUserName(user.getUserName());
         userVo.setUserPhone(user.getUserPhone());
         userVo.setAccount(user.getAccount());
         userVo.setAddress(user.getAddress());
         userVo.setImg(user.getImg());
         userVo.setRole(user.getRole());
         // System.out.println(saTokenInfo);
      //   System.out.println(StpUtil.getPermissionList(saTokenInfo.loginId));
          redisUtil.set("token:"+saTokenInfo.getTokenValue(),user.getUserId());
          redisTemplate.expire("token:"+saTokenInfo.getTokenValue(),1, TimeUnit.DAYS);
          return SaResult.ok("登录成功，会话id为"+StpUtil.getLoginId()+",Token为："+StpUtil.getTokenValue()).setData(userVo);
     }
       return  SaResult.error("登录失败！");
    }

    //注销登录
    @GetMapping("/loginOut")
    @SaCheckLogin
    public SaResult outLogin(){
        String loginId=null;
        if (StpUtil.isLogin()){
            SaTokenInfo saTokenInfo =StpUtil.getTokenInfo();
            loginId= (String) StpUtil.getLoginId();
            StpUtil.logout();
            redisUtil.del("token:"+saTokenInfo.getTokenValue());
        }
        return SaResult.ok("会话ID为 " + loginId + " 的用户注销登录成功");
    }
//注册
    @PostMapping("/register")
    @LogMethod
    public ResponseJsonMessage register(@RequestBody @Param("user") User user) {
       // System.out.println(user);
        loginService.registerUser(user);
    return    ResponseJsonMessage.ok();
    }
    /**
     * 根据Token值获取对应的账号id，如果未登录，则返回 null
     * @param tokenValue
     * @return
     */
    @RequestMapping("/getUserByToken/{tokenValue}")
    public SaResult getUserByToken(@PathVariable String tokenValue){
        return SaResult.ok((String) StpUtil.getLoginIdByToken(tokenValue));
    }
 //获取用户信息
    @GetMapping("/userInfo")
    @SaCheckLogin
    @SaCheckPermission("user-get")
    public ResponseJsonMessage getUserInfo( ){
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
  //修改用户信息
    @PostMapping("/userUpdate")
    @SaCheckLogin
    @SaCheckPermission("user-update")
    @LogMethod
    public ResponseJsonMessage updateUser(MultipartFile file,  @RequestBody @Param("user") User user){
        System.out.println(user);
        Optional<MultipartFile> optionalMultipartFile=optionalNull.isNullClass(file);
        optionalMultipartFile.ifPresent(file1 -> {
            try {
                String path=Fileutil.save(file);
                user.setImg(path);
                System.out.println(user.getImg());
            }catch (Exception e){
                throw new ComFoundException("图片上传失败");
            }
        });
        SaTokenInfo saTokenInfo =StpUtil.getTokenInfo();
        Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());
        user.setUserId(userId);
        Optional<User> optionalUser=optionalNull.isNullClass(user);
        User userVo=null;
      if (optionalUser.isPresent()){
          userVo=userService.updateUser(user);
          return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),userVo);
      }
        return new ResponseJsonMessage(ResponseJsonStatus.FAILURE.getStatus(), ResponseJsonStatus.FAILURE.getMessage(),"操作失败");
    }
//查询个人收藏
    @GetMapping("/userLike")
    @SaCheckLogin
    @SaCheckPermission("like-get")
    public ResponseJsonMessage userLike(){
        SaTokenInfo saTokenInfo =StpUtil.getTokenInfo();
        Integer userId= (Integer) redisTemplate.opsForValue().get("token:"+saTokenInfo.getTokenValue());
        List<Likes> likesList=likesService.getAllLikesByUser(userId);
        return  new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),likesList);
    }
    //查询商品收藏
    @GetMapping("/GoodLikeByUser")
    @LogMethod
    public ResponseJsonMessage userLikeByGood(@RequestParam("goodLike") Integer goodId){
        SaTokenInfo saTokenInfo =StpUtil.getTokenInfo();
        Integer userId= (Integer) redisTemplate.opsForValue().get("token:"+saTokenInfo.getTokenValue());

        boolean liked=likesService.getLikeByGood(goodId,userId);
        return  new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),liked);
    }
    //取消收藏
    @GetMapping("/updateLike")
    @SaCheckLogin
    @SaCheckPermission("like-delete")
    @LogMethod
    public ResponseJsonMessage updateLike(@RequestParam("likeId") Integer evaluateId){
        SaTokenInfo saTokenInfo =StpUtil.getTokenInfo();
        Integer userId= (Integer) redisTemplate.opsForValue().get("token:"+saTokenInfo.getTokenValue());
      int flag=likesService.deleteLikeByLikeId(evaluateId,userId);
        if (flag==0){
            throw new ComFoundException("删除失败，未收藏该商品！");
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage());
    }
     //-----------------------------------------------卖家------------------------------------------------------

    //申请成为卖家
    @GetMapping(value = "/applicant")
    public SaResult sellerApp(){
        SaTokenInfo saTokenInfo =StpUtil.getTokenInfo();
        String userId= (String) saTokenInfo.getLoginId();
     int flag=  userService.getSeller(Integer.parseInt(userId));
     if (flag==1){
         return SaResult.ok("正在申请");
     }
        return SaResult.error("申请失败");
    }
    //卖家上传商品
    @ResponseBody
    @SaCheckPermission("good-add")
    @PostMapping(value = "/seller/upload")
    @LogMethod
    public ResponseJsonMessage goodUpload(String files, @RequestParam("goods")  String goods){
        JSONObject jsonObject= JSON.parseObject(goods);
        List<String> result=new ArrayList<>();
        Goods good=new Goods();
        good.setCartTitle(jsonObject.getString("cartTitle"));
        good.setCartDescription(jsonObject.getString("cartDescription"));
        good.setGoodNum(jsonObject.getInteger("goodNum"));
        result.add(files);
        good.setUserId(jsonObject.getInteger("userId"));
        good.setPrice(jsonObject.getBigDecimal("price"));
        good.setCartImg(result);
        SaTokenInfo saTokenInfo=StpUtil.getTokenInfo();
        Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());
        good.setUserId(userId);
        goodsService.addGood(good);
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage());
    }
    @ResponseBody
    @SaCheckPermission("good-add")
    @PostMapping(value = "/seller/uploadImage")
    @LogMethod
    public ResponseJsonMessage goodImageUpload(@RequestParam("image") List<MultipartFile> files){
        List<String> result=new ArrayList<>();
        List<MultipartFile> multipartFiles=files;
        if (multipartFiles!=null){
            Iterator<MultipartFile> iterator=multipartFiles.iterator();
            //System.out.println(multipartFiles+"multipartFiles");
            while (iterator.hasNext()){
                MultipartFile photo=iterator.next();
                // System.out.println(photo.getOriginalFilename());
                try {
                    result.add(Fileutil.save(photo));
                    //  System.out.println(result.get(0)+"result"+result.get(1));
                    //    System.out.println(Fileutil.save(photo)+"fsave");
                }catch (Exception e){
                    throw new ComFoundException("上传图片失败");
                }
            }
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),result);
    }
 //修改商品
 @SaCheckPermission("good-update")
 @PostMapping("/seller/updateGood")
 @LogMethod
    public ResponseJsonMessage updateGood(String files,@RequestParam("goods")  Goods goods) {
   //  System.out.println(jsonObject+"--------------------------------------Json");
    String msg="修改商品失败！";
     List<String> result=new ArrayList<>();
         try {
             result.add(files);
             //  System.out.println(result.get(0)+"result"+result.get(1));
             //    System.out.println(Fileutil.save(photo)+"fsave");
         }catch (Exception e){
             throw new ComFoundException("上传图片失败");
         }

     goods.setCartImg(result);
     if (goods.equals(goods)){
         goodsService.updateGood(goods);
         return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),"修改成功");
     }
     return new ResponseJsonMessage(ResponseJsonStatus.FAILURE.getStatus(), ResponseJsonStatus.FAILURE.getMessage(),msg);

 }
 //
 @GetMapping("/seller/findGoods")
 @SaCheckRole("2")
 public ResponseJsonMessage findGoodBySeller(){
     SaTokenInfo saTokenInfo=StpUtil.getTokenInfo();
     Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());
     List<Goods> goods=goodsService.findAllGoodsByUser(userId);
     if (goods==null){
         return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),null);

     }
     return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),goods.size());
 }


  //查询卖家发布的商品
    @GetMapping("/seller/findGoodBySeller")
    @SaCheckRole("2")
    public ResponseJsonMessage findGoodsBySeller(){
        SaTokenInfo saTokenInfo=StpUtil.getTokenInfo();
        Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());
        List<Order> orderList=orderService.findOrderedBySeller(userId);
        if (orderList==null){
            return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),null);

        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),orderList);
    }

    //查询卖家发布的商品
 @GetMapping("/seller/findGoodsPage")
 @LogMethod
 public ResponseJsonMessage findGoodBySellerPage(@RequestParam("state") String state){
     SaTokenInfo saTokenInfo=StpUtil.getTokenInfo();
     Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());
     Integer startIndex=(Integer.valueOf(state)-1)*4;
     List<Goods> goodsList= goodsService.findAllGoodsBySeller(userId,Integer.valueOf(startIndex));
    // List<Goods> goods=goodsService.findAllGoodsByUser(userId);

     if (goodsList==null){
         return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),null);
     }
     return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),goodsList);
 }

    //卖家下架商品
    @SaCheckPermission("good-update")
    @GetMapping("/seller/deleteGood/{secondHandId}")
    @LogMethod
   public ResponseJsonMessage deleteGood(@PathVariable String secondHandId){
        System.out.println(secondHandId);
        Goods goods=new Goods();
        goods.setStatus("已下架");
       String msg="商品下架失败！";
        goods.setSecondHandId(Integer.valueOf(secondHandId));
       int f= goodsService.updateGood(goods);
       if (f!=1){
           return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),msg);

       }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),f);
   }

}
