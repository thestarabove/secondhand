package wtt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wtt.dao.*;
import wtt.pojo.*;
import wtt.utils.RedisIdWorker;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class SecondhandApplicationTests {
    @Autowired
    private UserDao userDao;
    @Autowired
    private GoodMapper goodMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private LikesMapper likesMapper;
    @Autowired
    private RatingformMapper ratingformMapper;
    @Autowired
    private RedisIdWorker redisIdWorker;

    private ExecutorService executorService=Executors.newFixedThreadPool(500);
    @Test
    void contextLoads() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(300);
        Runnable task=()->{
             for (int i = 0; i < 100; i++) {
                 long id = redisIdWorker.nextId("order");
                 System.out.println("id:"+id);
             }
             latch.countDown();
         };

         long beg=System.currentTimeMillis();
        for (int i = 0; i < 300; i++) {
            executorService.submit(task);
        }
        latch.await();
        long end=System.currentTimeMillis();
        System.out.println(end-beg);
    }
    //收藏表测试
//    Likes likes=likesMapper.selectByLikesId(1);
//        System.out.println(likes);
//    Order order=orderMapper.selectByOrderId(1);
//        System.out.println(order);

    //评价表测试
//        Ratingform ratingform=new Ratingform();
//        ratingform.setStarRating("4");
//        ratingform.setContent("shit");
//        ratingform.setState("已评价");
//        ratingform.setOrderId(1);
//        ratingform.setUserId(2);
//        ratingform.setEvaluateId(1);
//       //ratingformMapper.insert(ratingform);
//        ratingform=ratingformMapper.selectByEvaluateId(1);
//        System.out.println(ratingform);
//         Order order=orderMapper.selectByOrderId(ratingform.getOrderId());
//        System.out.println(order);


//    @Test
//    void UserTest(){
       //添加用户
  //    User user=new User();
//      user.setUserName("ppp");
//      user.setAccount("123");
//      user.setPassword("123");
//      user.setUserPhone("123456789");
//      user.setAddress("xxx");
//      user.setMoney(1000);
//      user.setRole(0);
//      userDao.insert(user);
        //查询用户
//       user=  userDao.selectById(1);
//        System.out.println(user);
        //修改用户
//        user.setUserId(1);
//        user.setUserPhone("111");
//        userDao.updateById(user);
        //删除用户
 //       user.setUserId(3);
 //      userDao.deleteById(3);
 //     user.setPassword("1");
        //md5加密
//     String md5Password= DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
//     System.out.println(md5Password);
 //   }

//商品测试
//    @Test
//    void GoodsTest(){
//   Goods goods=new Goods();
   //新增商品
//        goods.setCartTitle("iii");
//        goods.setCartImg("");
//        goods.setCartDescription("iii");
//        goods.setUserId(2);
//        goods.setPrice(BigDecimal.valueOf(11.0));
//        goodMapper.addGood(goods);
        //查询单个商品
//      goods=  goodMapper.findGoodById(1);
//        System.out.println(goods);
//        //修改商品
//        goods.setSecondHandId(2);
//        goods.setCartTitle("ww2");
//        goodMapper.updateSecondHandId(goods);
//        //删除商品
//        goodMapper.deleteBySecondHandId(764416004);
    //查询卖家所有商品
    //List<Goods> goods1=goodMapper.findGoodsByUserId(1);
    //for (Goods goods2:goods1){
    //  System.out.println(goods2);
    // }
//    }
//     订单测试
//    @Test
//    void orderTest(){
//        //新增订单
////      Order order=new Order();
////        order.setSecondHandId(2);
////        order.setUserId(2);
////        order.setState("审核中");
////        orderMapper.insertOrder(order);
//        //查询订单
////          order=orderMapper.selectByOrderId(1);
////        System.out.println(order);
////        //修改订单
////        order.setOrderId(1);
////        order.setState("通过");
////        orderMapper.updateByPrimaryKeySelective(order);
////        //删除订单
////        orderMapper.deleteByOrderId(1);
//        //查询订单信息
////        order=orderMapper.selectByOrderId(2);
////        System.out.println(order);
//    }
    //评价表测试
    @Test
    void evaluateTest(){
//        //提交评价表
     Ratingform ratingform=new Ratingform();
//
//        ratingform.setOrderId(2);
//        ratingform.setState("未评价");
//        ratingform.setStarRating("0");
//        ratingform.setContent("");
//        ratingform.setUserId(1);
//        ratingformMapper.insertRatingform(ratingform);
//        //查询评价
//        ratingform.setEvaluateId(1);
//        ratingform=ratingformMapper.selectByEvaluateId(1);
//        System.out.println(ratingform);
////        修改评价
//        ratingform.setEvaluateId(1);
//        ratingform.setState("已评价");
//        ratingformMapper.updateByPrimaryKeySelective(ratingform);
//        删除评价
       ratingform.setEvaluateId(2);


    }

}
