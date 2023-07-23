package wtt.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import wtt.Daomian.User;
import wtt.pojo.Goods;
import wtt.utils.RedisUtil;

@SpringBootTest
public class RedisTest {

    @Autowired
  private RedisUtil redisUtil;
    @Autowired
    private CartService cartService;
    @Autowired
    private GoodsService goodsService;
    @Test
    void contextLoad(){
    //  boolean f=cartService.isProductInCart("8","764416040");
      Goods goods=goodsService.findGood(764416040);

      cartService.handleProductInGoods(goods);
        System.out.println();
    }


}
