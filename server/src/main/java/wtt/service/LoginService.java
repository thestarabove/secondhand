package wtt.service;

import org.springframework.stereotype.Service;
import wtt.pojo.User;
import wtt.vo.UserVo;


public interface LoginService {
    public UserVo loginUser(String account, String password);
    public void registerUser(User user);
}
