package wtt.handler.server.serverImpl;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import wtt.handler.server.BasedHandler;
import wtt.utils.Roles;

import java.util.ArrayList;
import java.util.List;

@Service
public class SellerHandler extends BasedHandler {

    @Override
    public Roles getRole() {
        return Roles.SELLER;
    }

    @Override
    public List<String> doHandler() {
        List<String> sellerList=new ArrayList<>();
        sellerList.add("user-get");
        sellerList.add("user-update");
        sellerList.add("good-get");
        sellerList.add("good-add");
        sellerList.add("good-update");
        sellerList.add("like-add");
        sellerList.add("like-get");
        sellerList.add("like-delete");
        sellerList.add("cart-add");
        sellerList.add("cart-get");
        sellerList.add("cart-update");
        sellerList.add("cart-delete");
        return sellerList;
    }



    @Override
    public boolean validate() {
        if (getRole().getValue().equals("seller")){
            doHandler();
            return true;
        }
        return false;
    }
}
