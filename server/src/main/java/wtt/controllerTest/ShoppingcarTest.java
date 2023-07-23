package wtt.controllerTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wtt.pojo.ShoppingCar;
import wtt.service.CartService;
import wtt.utils.RedisUtil;
import wtt.utils.ResponseJsonStatus;
import wtt.vo.ResponseJsonMessage;

@RestController
@RequestMapping("/shopping")
public class ShoppingcarTest {
    private CartService cartService;
    @Autowired
    public void setRedisService(CartService cartService) {
        this.cartService = cartService;
    }
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/add")
    public ResponseJsonMessage addCart(){
        ShoppingCar car=new ShoppingCar();
        car.setUserId(4);
        car.setSecondHandId(3);
        car.setGoodAmount(1);
        cartService.addCart(car);

        return  new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage());
    }
    @GetMapping("/find")
    public ResponseJsonMessage findCart(){
        return  new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(), cartService.findAllProduct(4));
    }

    @GetMapping("/delete")
    public ResponseJsonMessage deleteCart(){
        cartService.delCartProduct(2,5);
        return  new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage());
    }
    @RequestMapping("doReduce")
    public ResponseJsonMessage doReduceProductNum(Integer userId, Integer productId) {
        cartService.productAmountDESC(4, 3);
        return  new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage());
    }


}
