package wtt.handler.server.serverImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import wtt.handler.server.BasedHandler;
import wtt.pojo.User;
import wtt.service.UserService;
import wtt.utils.Roles;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminHandler extends BasedHandler {
    @Autowired
    private UserService userService;
    @Override
    public Roles getRole() {
        return Roles.ADMIN;
    }
    @Override
    public List<String> doHandler() {
        List<String> adminList=new ArrayList<>();
        adminList.add("user-get");
        adminList.add("user-update");
        adminList.add("user-delete");
        adminList.add("good-delete");
        adminList.add("seller-add");
        adminList.add("user-getAll");
        return adminList;
    }

    @Override
    public boolean validate() {
        if (getRole().getValue().equals("admin")){
            doHandler();
            System.out.println("查验完成"+getRole().getValue());
            return true;
        }
        return false;
    }
}
