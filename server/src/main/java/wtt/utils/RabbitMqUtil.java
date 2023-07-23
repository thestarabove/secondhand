package wtt.utils;

import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wtt.config.DelayRabbitMqConfig;
import wtt.config.RabbitMqConfig;
import wtt.pojo.Goods;
import wtt.pojo.Order;
import wtt.service.GoodsService;
import wtt.service.OrderService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class RabbitMqUtil {
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;
    @RabbitListener(queues = {DelayRabbitMqConfig.DELAYED_QUEUE_NAME})

    public  void   receive_delay_order(Map msg) throws Exception{
        System.out.println("消息进入死信队列..."+new Date());
       Order order= (Order) msg.get("order");
       Integer goodNum= (Integer) msg.get("good");
        System.out.println(goodNum+"----------"+order);
         Goods goods=new Goods();
         goods.setGoodNum(goodNum);
         goods.setSecondHandId(order.getSecondHandId());
       try {
           Order order1=orderService.getOrderByOrderId(order.getOrderId());
           if (order1.getState().equals("待确认")){
               order.setState("关闭");
               System.out.println(order.getUpdateTime()+"关闭时间");
               orderService.closeOrder(order);
               goodsService.updateGood(goods);
//             //  channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
           }
       }catch (Exception e){

       }

    }

}
