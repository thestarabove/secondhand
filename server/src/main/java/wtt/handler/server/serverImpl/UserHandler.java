package wtt.handler.server.serverImpl;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import wtt.handler.server.BasedHandler;
import wtt.utils.Roles;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserHandler extends BasedHandler {

    @Override
    public Roles getRole() {
        return Roles.USER;
    }

    @Override
    public List<String> doHandler() {
        List<String> userList=new ArrayList<>();
        userList.add("user-get");
        userList.add("user-update");
        userList.add("good-get");
        userList.add("like-add");
        userList.add("like-get");
        userList.add("like-delete");
        userList.add("cart-add");
        userList.add("cart-get");
        userList.add("cart-update");
        userList.add("cart-delete");
        return userList;
    }


    @Override
    public boolean validate() {
        if (getRole().getValue().equals("user")){
            doHandler();
        return true;
        }
        return false;
    }
}
