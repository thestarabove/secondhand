package wtt.service;

import org.springframework.transaction.annotation.Transactional;
import wtt.pojo.Order;

import java.io.IOException;
import java.util.List;

public interface OrderService {
    public List<Order> getAllOrderByUser(Integer userId);
    public List<Order> getOrderByUser(Integer userId);
    public List<Order> getOrderedByUser(Integer userId);
    public Order getOrderByOrderId(Long orderId);
    int deleteOrder(Long orderId);
    int insertOrder(Order order);
    int updateOrder(Order order) ;
    int closeOrder(Order order);
    List<Order> findOrderedBySeller(Integer sellerId);
}
