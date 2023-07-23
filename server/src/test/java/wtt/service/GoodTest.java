package wtt.service;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wtt.dao.GoodMapper;
import wtt.pojo.Goods;
import wtt.service.serviceImpl.GoodsServiceImpl;
import wtt.utils.RedisClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class GoodTest {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodMapper goodMapper;
  @Autowired
  private RedisClient redisClient;
    @Test
    public void addGoodTest(){
        Goods goods=new Goods();
        goods.setCartTitle("xilin32");
        goods.setCartDescription("xilin32");
        goods.setPrice(BigDecimal.valueOf(112));
        goods.setUserId(31);
        List<String> strings=new ArrayList<>();
        strings.add("localhost:8081/images/java.util.Random@42b8dd58.png");
        strings.add("localhost:8081/images/java.util.Random@5549fe59.png");
        goods.setCartImg(strings);
        goods.setGoodNum(1999);
        goodsService.addGood(goods);
    }
    @Test
    public void findGood(){

    // goodsService.setExpire(8,"xilin");
//       // System.out.println(goods);
        List<Goods> page = goodMapper.findAllGoodsByPage("正在售卖", 10);
        System.out.println(page);
    }
     @Test
    public void findGoods(){

         List<Goods> goodsList=goodsService.findGoodsLock(8,"xilin");
         redisClient.setWithLogicalExpire("search:"+8,"xilin",goodsList,20L, TimeUnit.SECONDS);
         System.out.println(goodsList);
     }
     @Test
    public void updateGood(){
        Goods goods=new Goods();
        goods.setSecondHandId(764416030);
        goods.setPrice(BigDecimal.valueOf(111));
        List<String> strings=new ArrayList<>();
        strings.add("localhost:8081/images/java.util.Random@42b8dd58.png");
        strings.add("localhost:8081/images/java.util.Random@5549fe59.png");
        goods.setCartImg(strings);
        goods.setGoodNum(1990);
         goodsService.updateGood(goods);
     }

}
