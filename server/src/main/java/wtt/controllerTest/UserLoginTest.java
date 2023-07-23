package wtt.controllerTest;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wtt.pojo.User;
import wtt.service.LoginService;
import wtt.service.UserService;
import wtt.utils.ComFoundException;
import wtt.utils.RedisUtil;
import wtt.vo.ResponseJsonMessage;
import wtt.vo.UserVo;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/secondhand/user")
public class UserLoginTest {
    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping("/Login")
    public SaResult doLogin(@RequestBody @Param("data") Map<String,String> data){
        System.out.println(data);
        String account=data.get("account");
        String password=data.get("password");
       // String codeSession= (String) redisUtil.get("codeSession:code");
        //  System.out.println(codeSession+"------------codeSession");
//        if (!code.equals( codeSession)){
//            throw new ComFoundException("验证失败");
//        }
        System.out.println(account);
        UserVo user = loginService.loginUser(account,password);
        //System.out.println(user);
        //这个方法会强制在浏览器弹出一个认证框
        //  SaBasicUtil.check("sa:123456");
        if (user!=null){
            StpUtil.login(user.getUserId());
            SaTokenInfo saTokenInfo =StpUtil.getTokenInfo();
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
            return SaResult.ok("登录成功，会话id为"+StpUtil.getLoginId()+",Token为："+StpUtil.getTokenValue()).setData(userVo);
        }
        return  SaResult.error("登录失败！");
    }

    @PostMapping("/registered")
    public ResponseJsonMessage registered(@RequestBody @Param("user") User user) {
        System.out.println(user+"------------------------user");
        User user1 = new User();
        user1.setUserPhone(user.getUserPhone());
        user1.setUserName(user.getUserName());
        user1.setAddress(user.getAddress());
        user1.setAccount(user.getAccount());
        user1.setPassword(user.getPassword());
        loginService.registerUser(user1);
        return    ResponseJsonMessage.ok();
    }
}
