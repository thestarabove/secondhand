package wtt.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import wtt.dao.UserDao;
import wtt.pojo.User;
import wtt.service.LoginService;
import wtt.utils.ComFoundException;
import wtt.utils.Md5Util;
import wtt.vo.UserVo;

import java.lang.invoke.LambdaMetafactory;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
@Slf4j
@Service("LoginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserVo loginUser(String account, String password) {
        User user;
       if (account==null || password==null){
           System.out.println("输入账号或密码为空！");
       }
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper();
       lqw.eq(User::getAccount,account);
       user=userDao.selectOne(lqw);
       if (user==null){
           System.out.println("找不到用户信息");
       }
       String md5Password=setMd5(password);
       if (!user.getPassword().equals(md5Password)){
           log.info("密码不正确!");
       }
        UserVo userVo=new UserVo();
        BeanUtils.copyProperties(user,userVo);
        return userVo;
    }
//加密
    private String setMd5(String password){
        password=Md5Util.convertMD5(Md5Util.string2MD5(password));
    return password;
    }
    @Override
    public void registerUser(User user) {
        String account=user.getAccount();
        String password=user.getPassword();
        if (account==null || password==null){
            System.out.println("输入账号或密码为空！");
        }
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper();
        lqw.eq(User::getAccount,account);
      User user1=userDao.selectOne(lqw);
        if (user1!=null){
           throw new ComFoundException("用户已注册！");
        }
            String md5Password= setMd5(password);
            user.setPassword(md5Password);
            user.setAccount(account);
            user.setRole(0);
            user.setMoney(BigDecimal.valueOf(1000));
            Integer row=userDao.insert(user);
            if (row !=1){
                log.info("注册失败！");
            }else {
                log.info("注册成功！");
            }
        }


}
