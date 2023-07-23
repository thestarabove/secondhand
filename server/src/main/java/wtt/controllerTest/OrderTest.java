package wtt.controllerTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wtt.pojo.Order;
import wtt.service.OrderService;
import wtt.utils.ResponseJsonStatus;
import wtt.vo.ResponseJsonMessage;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderTest {
    @Autowired
    private OrderService orderService;
    @GetMapping("/getAllOrderByUser")
    public ResponseJsonMessage getAllOrderByUser(){
     List<Order> orderList= orderService.getAllOrderByUser(1);
       // System.out.println(orderList);
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),orderList);
    }
    @GetMapping("/getOrderByOrderId")
    public ResponseJsonMessage getOrderByOrderId(){
     Order order=orderService.getOrderByOrderId(2L);
        // System.out.println(orderList);
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),order);
    }

    @GetMapping("/update")
    public ResponseJsonMessage updateOrder(){
        Order order=new Order();
        order.setOrderId(2L);
        int f = 0;
            f = orderService.updateOrder(order);


        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),f);

    }


}
