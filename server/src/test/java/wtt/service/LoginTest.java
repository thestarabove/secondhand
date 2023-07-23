package wtt.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wtt.dao.UserDao;
import wtt.pojo.User;
import wtt.vo.UserVo;

@SpringBootTest
public class LoginTest {
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserDao userDao;

    @Test
    void registerUserTest(){
        User user=new User();
        user.setAccount("444");
        user.setPassword("111");
        user.setUserName("xilin");
        //user.setMoney(1000);
        user.setAddress("xilin");
        user.setUserPhone("101111");
        //System.out.println(user);
        loginService.registerUser(user);
    }
    @Test
    void loginTest(){
       String account="111";
       String password="111";
      UserVo user= loginService.loginUser(account,password);
       // System.out.println(user);
    }
}
