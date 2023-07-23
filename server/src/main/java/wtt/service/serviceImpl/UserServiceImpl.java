package wtt.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import wtt.dao.GoodMapper;
import wtt.dao.UserDao;
import wtt.pojo.User;
import wtt.service.UserService;
import wtt.utils.ComFoundException;
import wtt.vo.UserVo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service("UserService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private GoodMapper goodMapper;

    @Override
    public User getUser(Integer userId) {
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.eq(User::getUserId,userId);
        User user=userDao.selectOne(lqw);
        //String md5Password= DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
       // user.setPassword(md5Password);
        return user;
    }
    @Override
    public int getSeller(int id) {
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.eq(User::getUserId,id);
        User user=userDao.selectOne(lqw);
        //System.out.println(user.getUserId());
        if (user.getRole()==0){
            user.setRole(1);
            userDao.update(user,lqw);
            System.out.println("正在申请！");
        }
        return 1;
    }
    @Override
    public int Seller(int id) {
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.eq(User::getUserId,id);
        User user=userDao.selectOne(lqw);
        if (user.getRole()==1){
            user.setRole(2);
          userDao.update(user,lqw);
            System.out.println("恭喜你成为卖家！");
        }
        return 1;
    }
    @Override
    public int failSeller(int id) {
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.eq(User::getUserId,id);
        User user=userDao.selectOne(lqw);
        if (user.getRole()==1){
            user.setRole(0);
            userDao.update(user,lqw);
            System.out.println("审核不通过！");
        }
        return 1;
    }
    @Override
    public List<User> getAllUser() {
        List<User> userList=userDao.selectList(null);
        return userList;
    }

    @Override
    public List<UserVo> getUserBySeller() {
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<User>()
                .select(User.class, info -> !info.getProperty().equals("password"));
        Integer role=1;
        lqw.eq(User::getRole,role);
        List<User> userList=userDao.selectList(lqw);
        List<UserVo> collect = userList.stream()
                .map(u -> {
                    UserVo userVo = new UserVo();
                    BeanUtils.copyProperties(u,userVo);
                    return userVo;
                })
                .collect(Collectors.toList());
        return collect;
    }
    @Override
    public void deleteUser(int id) {
        LambdaQueryWrapper<User> lqw=new LambdaQueryWrapper<>();
        lqw.eq(User::getUserId,id);
       goodMapper.deleteBySellerId(id);
        int row=userDao.delete(lqw);
        if (row!=1){
            System.out.println("删除失败！");
        }else {
            System.out.println("删除成功！");
        }
    }

    @Override
    public User updateUser(User user) {
        if (user.getUserId()!=null){
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getUserId, user.getUserId());
            userDao.update(user, lqw);
            User user1 = userDao.selectOne(lqw);
            return user1;
        }else {
            log.info("userId为空");
        }
//        boolean modified = false;
//        if (user.getUserName() != null && !user.getUserName().isEmpty()) {
//            user1.setUserName(user.getUserName());
//            modified = true;}
//        if (user.getUserPhone() != null && !user.getUserPhone().isEmpty()) {
//            user1.setUserPhone(user.getUserPhone());
//            modified = true;}
//        if (user.getAddress() != null && !user.getAddress().isEmpty()) {
//            user1.setAddress(user.getAddress());
//            modified = true;}
//        if (user.getImg() != null && !user.getImg().isEmpty()) {
//            user1.setImg(user.getImg());
//            modified = true;}
//        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
//            user1.setPassword(user.getPassword());
//            modified = true;}
//
//        if (modified) {
//            try {
//                userDao.update(user1, lqw);
//                System.out.println("修改成功！");
//            } catch (Exception e) {
//                throw new ComFoundException("修改失败！");}
//        } else {
//            System.out.println("没有需要修改的属性。");
//        }
        return null;
    }
}
