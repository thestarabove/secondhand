package wtt.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import wtt.dao.OrderMapper;
import wtt.dao.RatingformMapper;
import wtt.dao.UserDao;
import wtt.pojo.Order;
import wtt.pojo.Ratingform;
import wtt.pojo.User;
import wtt.service.OrderService;
import wtt.utils.ComFoundException;
import wtt.utils.RedisIdWorker;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service("OrderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserDao userDao;
    @Autowired
    private RatingformMapper ratingformMapper;
    private RedisIdWorker redisIdWorker;

    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public List<Order> getAllOrderByUser(Integer userId) {
        List<Order> orderList=orderMapper.selectAllByUserIdOrder(userId);
        //System.out.println(orderList);
        if (orderList==null||orderList.size()==0){
            throw new ComFoundException("用户未购买商品");
            //System.out.println("用户未购买商品");
        }
        return orderList;
    }

    @Override
    public List<Order> findOrderedBySeller(Integer sellerId) {
        String status="已完成";
        List<Order> list=orderMapper.selectOrderedBySeller(sellerId,status);
        for (Order o:list) {
            BigDecimal total = new BigDecimal(o.getTotal());
            BigDecimal price = new BigDecimal(String.valueOf(o.getGoods().getPrice()));
            BigDecimal num = total.divide(price, RoundingMode.HALF_UP).setScale(0, RoundingMode.DOWN);; // 使用半舍入模式进行除法计算
            o.setNum(num.toString());
        }
        if (list==null){
            return null;
        }
        return list;
    }

    @Override
    public List<Order> getOrderByUser(Integer userId) {
        String state="待确认";
        List<Order> orderList=orderMapper.selectByUserIdOrder(userId,state);
        for (Order o:orderList) {
            BigDecimal total = new BigDecimal(o.getTotal());
            BigDecimal price = new BigDecimal(String.valueOf(o.getGoods().getPrice()));
            BigDecimal num = total.divide(price, RoundingMode.HALF_UP).setScale(0, RoundingMode.DOWN);; // 使用半舍入模式进行除法计算
            o.setNum(num.toString());
        }
        if (orderList==null||orderList.size()==0){
           // throw new ComFoundException("用户未购买商品");
            //System.out.println("用户未购买商品");
        }
        return orderList;
    }

    @Override
    public List<Order> getOrderedByUser(Integer userId) {
        String state="已完成";
        List<Order> orderList=orderMapper.selectByUserIdOrder(userId,state);
        for (Order o:orderList) {
           // System.out.println(o);
            if (o.getTotal()!=null &&!o.getTotal().equals("")){
                BigDecimal total = new BigDecimal(o.getTotal());
                BigDecimal price = new BigDecimal(String.valueOf(o.getGoods().getPrice()));
                BigDecimal num = total.divide(price, RoundingMode.HALF_UP).setScale(0, RoundingMode.DOWN);; // 使用半舍入模式进行除法计算
                o.setNum(num.toString());
            }else {
                o.setNum("0");
            }
        }
        if (orderList==null||orderList.size()==0){
            throw new ComFoundException("用户未购买商品");
            //System.out.println("用户未购买商品");
        }
        return orderList;
    }

    @Override
    public Order getOrderByOrderId(Long orderId) {
       Order order=orderMapper.selectByOrderId(orderId);
        if (order==null){
            throw new ComFoundException("用户未购买商品");
        }
        return order;
    }

    @Override
    public int deleteOrder(Long orderId) {
         int flag=orderMapper.deleteByOrderId(orderId);
         if (flag==0){
             throw new ComFoundException("删除订单失败！");
         }
         return flag;
    }

    @Override
    public int insertOrder(Order order) {
        order.setState("待确认");
        order.setOrderId(redisIdWorker.nextId("order"));
        int flag=orderMapper.insertOrder(order);
        if (flag==0){
            throw new ComFoundException("新增订单失败！");
        }

        return flag;
    }

    @Override
    public int closeOrder(Order order) {
        int flag=orderMapper.updateByPrimaryKeySelective(order);
        if (flag==0){
            throw new ComFoundException("关闭订单失败！");
        }

        return flag;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
    public int updateOrder(Order order)  {
        if (!order.getState().equals("待确认")){
            throw new ComFoundException("获取错误的订单信息");
        }
        order.setState("已完成");
        Ratingform ratingform=new Ratingform();
        ratingform.setStarRating("0");
        ratingform.setState("未评价");
        ratingform.setUserId(order.getUserId());
        ratingform.setOrderId(order.getOrderId());
        //买家支出
        User user=new User();
        user=userDao.selectById(order.getUserId());
        BigDecimal total = new BigDecimal(order.getTotal());
        user.setMoney(user.getMoney().subtract(total));
       // System.out.println(user.getMoney());
        userDao.updateById(user);

       // System.out.println(1/0);
        //卖家收入
        User user1=new User();
        user1=userDao.selectById(order.getGoods().getUserId());
        System.out.println(order.getGoods().getUserId());
        user1.setMoney(user1.getMoney().add(total));
        System.out.println(user1.getMoney());
        userDao.updateById(user1);

        int flag=orderMapper.updateByPrimaryKeySelective(order);
        if (flag==0){
            throw new ComFoundException("商品交易失败！");
        }
        if (order.getUserId()!=null){
            ratingformMapper.insertRatingform(ratingform);
        }
        return flag;
    }


}
