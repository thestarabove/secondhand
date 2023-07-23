package wtt.utils;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wtt.handler.factoryHandler.Factory;
import wtt.handler.server.BasedHandler;
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
             BasedHandler basedHandler= (BasedHandler) Factory.getInvokeStrategy("user");
             List<String> doHandler = basedHandler.doHandler();
             stringList=doHandler;
         }else if (user.getRole()==2){
             BasedHandler basedHandler= (BasedHandler) Factory.getInvokeStrategy("seller");
             List<String> doHandler = basedHandler.doHandler();
             stringList=doHandler;
         }else {
             BasedHandler basedHandler= (BasedHandler) Factory.getInvokeStrategy("admin");
             List<String> doHandler = basedHandler.doHandler();
             stringList=doHandler;

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
