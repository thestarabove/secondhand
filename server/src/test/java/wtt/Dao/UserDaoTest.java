package wtt.Dao;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wtt.dao.UserDao;
import wtt.pojo.User;

@SpringBootTest
public class UserDaoTest {
 @Autowired
 private UserDao userDao;
    @Test
    public void addUser(){
        User user=new User();
        user.setUserName("xilin");
        userDao.insert(user);
        // System.out.println(user1);
    }
    @Test
    public void updateUser(){
        User user=new User();
        user.setUserName("xilin");
        user.setUserId(22);
        userDao.update(user,null);
    }
    @Test
    public void deleteUser(){
        User user=new User();
        user.setUserId(31);
        userDao.deleteById(user);
    }
}
