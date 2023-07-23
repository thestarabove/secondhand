package wtt.utils;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wtt.pojo.User;
import wtt.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Component
public class Role implements StpInterface {
    @Autowired
    private UserService userService;
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> stringList=new ArrayList<>();
        String userId= (String) loginId;
        User user=userService.getUser(Integer.valueOf(userId));
        //System.out.println(user.getRole());
         if (user.getRole()==0||user.getRole()==1){
             stringList.add("user-get");
             stringList.add("user-update");
             stringList.add("good-get");
             stringList.add("like-add");
             stringList.add("like-get");
             stringList.add("like-delete");
             stringList.add("cart-add");
             stringList.add("cart-get");
             stringList.add("cart-update");
             stringList.add("cart-delete");
         }else if (user.getRole()==2){
             stringList.add("user-get");
             stringList.add("user-update");
             stringList.add("good-get");
             stringList.add("good-add");
             stringList.add("good-update");
             stringList.add("like-add");
             stringList.add("like-get");
             stringList.add("like-delete");
             stringList.add("cart-add");
             stringList.add("cart-get");
             stringList.add("cart-update");
             stringList.add("cart-delete");
         }else {
             stringList.add("user-get");
             stringList.add("user-update");
             stringList.add("user-delete");
             stringList.add("good-delete");
             stringList.add("seller-add");
             stringList.add("user-getAll");
         }
        System.out.println("用户权限列表：" + stringList);

        return stringList;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> stringList=new ArrayList<>();
        String userId= (String) loginId;
        User user=userService.getUser(Integer.valueOf(userId));
        if (user.getRole()==0){
            stringList.add("0");
        }else if (user.getRole()==1){
            stringList.add("1");
        }else if (user.getRole()==2){
            stringList.add("2");
        }else if (user.getRole()==3){
            stringList.add("3");
        }
        return stringList;

    }
}
