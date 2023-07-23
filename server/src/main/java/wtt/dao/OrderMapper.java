package wtt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import wtt.pojo.Order;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    int deleteByOrderId(Long orderId);

    //会覆盖默认值
    int insertOrder(Order record);
    //新增不会改变默认值
    int insertSelective(Order record);

    Order selectByOrderId(Long orderId);

    List<Order> selectAllByUserIdOrder(Integer userId);
    List<Order> selectByUserIdOrder(Integer userId,String state);
    List<Order> selectOrderedBySeller(Integer sellerId,String state);

    int updateByPrimaryKeySelective(Order record);

    int updateByOrderId(Order record);
}