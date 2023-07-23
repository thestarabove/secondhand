package wtt.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wtt.pojo.Likes;
import wtt.pojo.User;
import wtt.service.serviceImpl.LoginServiceImpl;
import wtt.utils.Md5Util;
import wtt.vo.UserVo;

import java.util.List;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserService userService;
    @Autowired
    private LikesService likesService;
    @Autowired
    private LoginService loginService;

    @Test
    public void getUserTest(){
     User user=userService.getUser(1);
        System.out.println(user);
    }
    @Test
    public void getAllUserTest(){
       List<User> user= userService.getAllUser();
        System.out.println(user);
    }
    @Test
    public void getSeller(){
   int user= userService.getSeller(1);
        System.out.println(user);
    }
    @Test
    public void getSellers(){
        List<UserVo> user= userService.getUserBySeller();
        System.out.println(user);
    }
   @Test
    public void Seller(){
        int f=userService.Seller(1);
       System.out.println(f);
   }
   @Test
    public void deleteUser(){
        userService.deleteUser(23);
   }

   @Test
    public void updateUser(){
        User user=new User();
        user.setUserName("xilin");
        user.setUserId(24);
        user.setRole(0);
        user.setAddress("111");
       User user1=userService.updateUser(user);
      // System.out.println(user1);
   }
   @Test
    public void addLikeTest(){
       Likes likes=new Likes();
       likes.setUserId(8);
       likes.setSecondHandId(764416032);
       likesService.addLikeByGoodId(likes);
   }
    @Test
    public void updateLikeTest(){
        likesService.deleteLikeByLikeId(24,8);
    }
}
