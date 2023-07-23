package wtt.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wtt.dao.GoodMapper;
import wtt.dao.OrderMapper;
import wtt.dao.RatingformMapper;
import wtt.pojo.Order;
import wtt.pojo.Ratingform;
import wtt.service.OrderService;
import wtt.service.RatingformService;
import wtt.utils.ComFoundException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service("RatingFormService")
public class RatingFormServiceImpl implements RatingformService {
    @Autowired
    private RatingformMapper ratingformMapper;
    @Autowired
    private OrderMapper orderMapper;
    //获取用户未评价的评价表
    @Override
    public List<Ratingform> getAllRatingFormByUser(Integer userId) {
        List<Ratingform> ratingformList=ratingformMapper.selectAllByUserIdRatingforms(userId);
        List<Ratingform> ratingformList1 = new LinkedList<>();
        for (Ratingform ratingform:ratingformList){
           // System.out.println(ratingform);
           // System.out.println(ratingform.getState().equals("未评价"));
            if (ratingform.getState().equals("未评价")){
         Long orderId=ratingform.getOrderId();
            Order order=orderMapper.selectByOrderId(orderId);
               // System.out.println(order+"x");
            ratingform.setOrder(order);
            ratingformList1.add(ratingform);

            }
        }
        return  ratingformList1;
    }

    @Override
    public List<Ratingform> getRatingFormByEvaluated(Integer userId) {
        List<Ratingform> ratingformList=ratingformMapper.selectAllByUserIdRatingforms(userId);
        List<Ratingform> ratingformList1 = new LinkedList<>();
        for (Ratingform ratingform:ratingformList){
            // System.out.println(ratingform);
            // System.out.println(ratingform.getState().equals("未评价"));
            if (ratingform.getState().equals("已评价")){
                Long orderId=ratingform.getOrderId();
                Order order=orderMapper.selectByOrderId(orderId);
                // System.out.println(order+"x");
                ratingform.setOrder(order);
                ratingformList1.add(ratingform);
                break;
            }
        }
        return  ratingformList1;
    }

    @Override
    public boolean addRatingFormByUserId(Integer userId,Integer orderId) {
        return false;
    }

    @Override
    public int deleteRatingFormByEvaluateId(Integer evaluateId) {
        return 0;
    }

    @Override
    public int updateRatingFormByEvaluateId(Ratingform ratingform) {
        ratingform.setState("审核中");
       Ratingform ratingform1= ratingformMapper.selectByEvaluateId(ratingform.getEvaluateId());
       if (ratingform1==null){
          return 0;
       }else if (ratingform1.getState().equals("已评价")){
           return 2;
       }
        try {
             ratingformMapper.updateByPrimaryKeySelective(ratingform);
        }catch (ComFoundException c){
            throw new ComFoundException("评价失败！");
        }
        return 1;
    }

    @Override
    public List<Ratingform> findFormByAdmin() {
        String status="审核中";
        LambdaQueryWrapper<Ratingform> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Ratingform::getState,status);
        List<Ratingform> list=ratingformMapper.selectList(lambdaQueryWrapper);
        if (list==null){
            return null;
        }
        return list;
    }

    @Override
    public int updateRatingFormByAdmin(Integer ratingId) {
        Ratingform ratingform=ratingformMapper.selectByEvaluateId(ratingId);
        if (ratingform==null){
            return 0;
        }else if (ratingform.getState().equals("已评价")){
            return 2;
        }else {
            try {
                ratingform.setState("已评价");
                ratingformMapper.updateByPrimaryKeySelective(ratingform);
                return 1;
            }catch (ComFoundException c){
                throw new ComFoundException("评价失败！");
            }
        }
    }
    @Override
    public int failRatingFormByAdmin(Integer ratingId) {
        Ratingform ratingform=ratingformMapper.selectByEvaluateId(ratingId);
        if (ratingform==null){
            return 0;
        }else if (ratingform.getState().equals("已评价")){
            return 2;
        }else {
            try {
                ratingform.setState("关闭");
                ratingformMapper.updateByPrimaryKeySelective(ratingform);
                return 1;
            }catch (ComFoundException c){
                throw new ComFoundException("评价失败！");
            }
        }
    }
}
