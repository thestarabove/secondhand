package wtt.service;

import wtt.pojo.User;
import wtt.vo.UserVo;

import java.util.List;

public interface UserService {
   public List<User> getAllUser();
   public List<UserVo> getUserBySeller();
   public User getUser(Integer userId);
   int Seller(int id);
   int failSeller(int id);
   int getSeller(int id);
   void deleteUser(int id);
   User updateUser(User user);

}
