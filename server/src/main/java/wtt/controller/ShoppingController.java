package wtt.controller;



import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.util.UUIDUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wtt.config.DelayRabbitMqConfig;
import wtt.config.JacksonConfig;
import wtt.config.RabbitMqConfig;
import wtt.pojo.*;
import wtt.service.*;
import wtt.utils.ComFoundException;
import wtt.utils.RedisUtil;
import wtt.utils.ResponseJsonStatus;
import wtt.vo.OrderVo;
import wtt.vo.ResponseJsonMessage;
import wtt.vo.ShoppingCarVo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;


//购物车，提交订单
@RestController
@RequestMapping("/secondhand/cart")
public class ShoppingController {
    @Autowired
    private CartService cartService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private RatingformService ratingformService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @GetMapping("/addCart")
    @SaCheckPermission("cart-add")
    public ResponseJsonMessage addCart(@RequestParam("secondHandId") Integer secondHandId){
       Goods goods= goodsService.findGood(secondHandId);
        ShoppingCar shoppingCar=new ShoppingCar();
        Integer amount=1;
        shoppingCar.setSecondHandId(goods.getSecondHandId());
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
       // String userId= (String) saTokenInfo.getLoginId();
        Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());
        shoppingCar.setUserId(userId);
        shoppingCar.setGoodAmount(amount);
       UUID uuid=UUID.randomUUID();
        shoppingCar.setCarId(uuid.toString().trim().replaceAll("-",""));
      int f=cartService.addCart(shoppingCar);
        if (f!=1){
            throw new ComFoundException("加入购物车失败！");
        }
        return new  ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(),ResponseJsonStatus.SUCCESS.getMessage(),f);
    }
    @GetMapping("/findCart")
    @SaCheckPermission("cart-get")
    public ResponseJsonMessage findCarts(){
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        //String userId= (String) saTokenInfo.getLoginId();
        Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());
        List<ShoppingCarVo> shoppingCarVos=cartService.findAllProduct(userId);
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),shoppingCarVos);
    }

    @GetMapping("/amountASC")
    public ResponseJsonMessage amountAsc(@RequestParam("GoodId") Integer goodId){
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        String userId= (String) saTokenInfo.getLoginId();
        int f=cartService.productAmountASC(Integer.valueOf(userId),goodId);
        if (f!=1){
            throw new ComFoundException("增加商品失败");
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),f);
    }
    @GetMapping("/amountDESC")
    public ResponseJsonMessage amountDesc(@RequestParam("GoodId") Integer goodId){
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        String userId= (String) saTokenInfo.getLoginId();
       int f=cartService.productAmountASC(Integer.valueOf(userId),goodId);
       if (f!=1){
           throw new ComFoundException("减少商品失败");
       }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),f);
    }
    @GetMapping("/amount")
    public ResponseJsonMessage amount(@RequestParam("GoodId") Integer goodId,@RequestParam("Num") Integer num){
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());
        int f=cartService.productAmount(userId,goodId,num);
        if (f!=1){
            throw new ComFoundException("减少商品失败");
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),f);
    }
   @GetMapping("/deleteCart")
   @SaCheckPermission("cart-delete")
   public ResponseJsonMessage deleteCart(@RequestParam("secondHandId") Integer secondHandId){
        SaTokenInfo saTokenInfo=StpUtil.getTokenInfo();
       Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());
        int f =cartService.delCartProduct(userId,secondHandId);
        if (f!=1){
            throw new ComFoundException("删除购物车失败！");
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),f);
   }
    @GetMapping("/addOrder")
    public ResponseJsonMessage addOrder(){
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());

        List<ShoppingCarVo> shoppingCarVos=cartService.findAllProduct(userId);
        Order order=new Order();
        for (ShoppingCarVo s : shoppingCarVos) {
             order.setSecondHandId(s.getShoppingCar().getSecondHandId());
             order.setUserId(s.getShoppingCar().getUserId());
             orderService.insertOrder(order);
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage());
    }
    @GetMapping("/sendOrder")
    public ResponseJsonMessage sendOrder(@RequestParam String secondHandId){
        List<String> findGoods=Arrays.asList(secondHandId.split(","));
        //System.out.println(findGoods);
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());
        List<ShoppingCarVo> shoppingCarVos=cartService.findProducts(userId,findGoods);
    //    System.out.println(shoppingCarVos+"-------------------------------------shoppingCarVo");
        List<Long> orderIds=new ArrayList<>();
        Order order=new Order();
        for (ShoppingCarVo s : shoppingCarVos) {
            Goods goods=goodsService.findGood(s.getShoppingCar().getSecondHandId());
            Integer goodNum=goods.getGoodNum();
           Integer num=goods.getGoodNum()-s.getNum();
           if (num<0){
               throw new ComFoundException("下单失败,商品数量不够！");
           }
            order.setSecondHandId(s.getShoppingCar().getSecondHandId());
            order.setUserId(s.getShoppingCar().getUserId());
            BigDecimal total=goods.getPrice().multiply(BigDecimal.valueOf(s.getNum()));
            order.setTotal(String.valueOf(total));
            order.setSellerId(goods.getUserId());
            orderService.insertOrder(order);
            //System.out.println(goods);
            goods.setGoodNum(num);
            goodsService.updateGood(goods);
            System.out.println("OrderController.submit... createOrder"+new Date());
            orderIds.add(order.getOrderId());
            String redisOrderId= String.valueOf(order.getOrderId());
           order.setState("待确认");
             Map<String,Object> map=new HashMap<>();
             map.put("order",order);
             map.put("good",goodNum);
        rabbitTemplate.convertAndSend(DelayRabbitMqConfig.DELAYED_EXCHANGE_NAME,DelayRabbitMqConfig.DELAYED_ROUTING_KEY,map, message -> {
                message.getMessageProperties().setDelay(1000*60*10);
                return message;
            });
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),orderIds);
    }
    @GetMapping("/findOrder")
    public ResponseJsonMessage findOrder(){
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());
         List<Order> list=orderService.getOrderByUser(userId);
          if (list==null){
              return new ResponseJsonMessage(ResponseJsonStatus.FAILURE.getStatus(), ResponseJsonStatus.FAILURE.getMessage(), null);
          }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),list);
    }
    @GetMapping("/findOrdered")
    @SaCheckLogin
    public ResponseJsonMessage findOrdered(){
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());
        //System.out.println(userId);
        List<Order> list=orderService.getOrderedByUser(userId);

        if (list==null){
            return new ResponseJsonMessage(ResponseJsonStatus.FAILURE.getStatus(), ResponseJsonStatus.FAILURE.getMessage(), null);
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),list);
    }

    @GetMapping("/complete")
    public ResponseJsonMessage complete(@RequestParam("orderId") Long orderId){
       Order order=  orderService.getOrderByOrderId(orderId);
        System.out.println(order+"....................order");
          int f =orderService.updateOrder(order);
       if (f!=1){
           throw new ComFoundException("提交订单失败！");
       }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),f);
    }

    @GetMapping("/findRating")
    public ResponseJsonMessage findRating(){
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());
        List<Ratingform>  ratingformList=ratingformService.getAllRatingFormByUser(userId);
        if (ratingformList==null){
            return new ResponseJsonMessage(ResponseJsonStatus.FAILURE.getStatus(), ResponseJsonStatus.FAILURE.getMessage(), null);
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),ratingformList);
    }
    @GetMapping("/findRated")
    public ResponseJsonMessage findRated(){
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());
        List<Ratingform>  ratingformList=ratingformService.getRatingFormByEvaluated(userId);
        if (ratingformList==null){
            return new ResponseJsonMessage(ResponseJsonStatus.FAILURE.getStatus(), ResponseJsonStatus.FAILURE.getMessage(), null);
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),ratingformList);
    }
    @PostMapping("/completeRating")
    public ResponseJsonMessage completeRating(@RequestParam("rating") String rating){
        JSONObject jsonObject= JSON.parseObject(rating);
        System.out.println(jsonObject);
        Ratingform ratingform=new Ratingform();
        ratingform.setEvaluateId(jsonObject.getInteger("evaluateId"));
        ratingform.setStarRating(jsonObject.getString("rate"));
        ratingform.setContent(jsonObject.getString("formDetail"));
       ratingformService.updateRatingFormByEvaluateId(ratingform);
        System.out.println(ratingform+"....................order");
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage());
    }


}
