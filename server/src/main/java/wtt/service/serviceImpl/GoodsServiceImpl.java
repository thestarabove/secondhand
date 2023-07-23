package wtt.service.serviceImpl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import wtt.config.RedissonConfig;
import wtt.dao.GoodMapper;
import wtt.pojo.Goods;
import wtt.pojo.User;
import wtt.service.CartService;
import wtt.service.GoodsService;
import wtt.service.SearchRedisService;
import wtt.service.UserService;
import wtt.utils.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service("GoodsService")
@Slf4j
public class GoodsServiceImpl implements GoodsService {
    private static final String GOOD_REDIS_NAME="search:";
    private static final String GOOD_LOCK_REDIS_NAME="lock:search:";
    @Autowired
    private  CartService cartService;
    @Autowired
    private GoodMapper goodMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private RedissonConfig redissonConfig;
    @Override
    public Goods findGood(Integer second_hand_id) {
        Goods goods=null;
        try {
             goods=goodMapper.findGoodById(second_hand_id);
        }catch (Exception e){
            throw new ComFoundException("查询失败！");
        }
        return goods;
    }
    @Override
    public List<Goods> findGoodsByPage(Integer state){
        String status="正在售卖";
        List<Goods> goods=goodMapper.findAllGoodsByPage(status,state);
        //System.out.println(goods+"-------------------------goodsList");
        return goods;
    }

    @Override
    public List<Goods> findAllGoodsByUser(Integer userId) {
        String status="正在售卖";
        List<Goods> goods=goodMapper.findGoodsByUserId(userId,status);
        return goods;
    }
//    查询 防止缓存穿透商品
    @Override
    public List<Goods> findGoodByNamePage(Integer userId, String name) {

        RBloomFilter<Object> rBloomFilter=redissonConfig.redissonClient().getBloomFilter("bloom-filter-search");
        rBloomFilter.tryInit(100000L,0.01);
        List<User> userList=userService.getAllUser();
        for (User u :userList) {
            rBloomFilter.add(u.getUserId());
        }
        if (!rBloomFilter.contains(userId)){
             log.error("所要查询的数据既不在缓存中，也不在数据库中，为非法key");
            redisTemplate.opsForHash().put(RedisName.GOOD_REDIS_NAME+userId,name,"");
            redisTemplate.expire(userId,30L,TimeUnit.MINUTES);
        }
        List<Goods> goodsList=  redisClient.queryWithPassThroughBloom(GOOD_REDIS_NAME,userId,name,Goods.class,this::findGoods,30L,TimeUnit.MINUTES);
        return goodsList;
    }
    public List<Goods> findGoods(String name){
        String status="正在售卖";
     List<Goods> goodsList=goodMapper.findGoodsByNamePage(status,name);
     return goodsList;
    }

    //逻辑过期
    @Override
    public List<Goods> findGoodsLock(Integer userId, String name) {
        String status="正在售卖";
     List<Goods> goodsList=  redisClient.queryWithLogicalExpire(GOOD_REDIS_NAME,userId,name,Goods.class,this::findGoods,30L,TimeUnit.MINUTES);
       // List<Goods> goodsList=this.queryWithTime(userId,name);
        if (goodsList==null){
            goodsList=goodMapper.findGoodsByNamePage(status,name);
         return goodsList;
     }
     return goodsList;
    }
    @Override
    public List<Goods> findAllGoods() {
        List<Goods> goodsList;
        String status="正在售卖";
      LambdaQueryWrapper<Goods> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Goods::getStatus,status);
        try {
            goodsList=goodMapper.selectList(lambdaQueryWrapper);
        }catch (Exception e){
            throw  e;
        }
       // System.out.println(goodsList);
        return goodsList;
    }

    @Override
    public List<Goods> findAllGoodsBySeller(Integer userId,Integer state) {
        String status="正在售卖";
        List<Goods> goodsList=goodMapper.findAllGoodsBySeller(userId,status,state);
      //  System.out.println(goodsList+"----------------------------------goodList");
        return goodsList;
    }

    @Override
    public int addGood(Goods goods) {
        goods.setStatus("正在售卖");
        boolean r = goodMapper.addGood(goods);
        int flag;
            if (!r){
                System.out.println("发布失败！");
                flag=0;
            }else {
                System.out.println("发布成功！");
                flag=1;
            }
            return flag;
    }
    @Override
    public int updateGood(Goods goods) {
        Goods goods1=new Goods();
        //System.out.println(goods+"-------------------------------goods");
        goods1=goodMapper.findGoodById(goods.getSecondHandId());
        if (goods.getCartTitle()!=null&&!goods.getCartTitle().equals("")){
           goods1.setCartTitle(goods.getCartTitle());
        }
        if (goods.getCartDescription()!=null&&!goods.getCartDescription().equals("")){
            goods1.setCartDescription(goods.getCartDescription());
        }
        if (goods.getCartImg() != null && !goods.getCartImg().contains(null) && goods.getCartImg().size() > 0) {
            goods1.setCartImg(goods.getCartImg());
        }
        goods1.setUpdateTime(null);
       boolean g=goodMapper.updateSecondHandId(goods1);
        int flag;
        if (!g){
            System.out.println("商品修改失败！");
            flag=0;
        }else {
            System.out.println("商品修改成功！");
            String goodId= String.valueOf(goods.getSecondHandId());
           boolean f=cartService.handleProductInGood(goodId);
           // System.out.println(f+"------------------------------------------cart缓存");
            Goods goods2=new Goods();
            goods2=goodMapper.findGoodById(goods.getSecondHandId());
           if (goods2.getStatus().equals("正在售卖")){
               cartService.handleProductInGoods(goods2);
               System.out.println("商品重新上传");
           }
            flag=1;
        }
        return flag;
    }

    @Override
    public int deleteGood(Integer id) {
      boolean g= goodMapper.deleteBySecondHandId(id);
        int flag;
        if (!g){
            System.out.println("删除失败！");
            flag=0;
        }else {
            System.out.println("删除成功！");
            flag=1;
        }
        return flag;
    }
}
