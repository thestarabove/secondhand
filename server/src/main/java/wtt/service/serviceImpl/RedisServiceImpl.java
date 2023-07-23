package wtt.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import wtt.pojo.Goods;
import wtt.pojo.ShoppingCar;
import wtt.service.CartGoodService;
import wtt.service.CartService;
import wtt.service.GoodsService;
import wtt.utils.RedisUtil;
import wtt.vo.ShoppingCarVo;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("CartService")
public class RedisServiceImpl implements CartService {
    private static  final String CART_KEY_PREFIX="cart:";
    @Autowired
    @Lazy
    private  GoodsService goodsService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public int addCart(ShoppingCar shoppingCar) {
        String userId= String.valueOf(shoppingCar.getUserId());
        String goodId= String.valueOf(shoppingCar.getSecondHandId());
       // System.out.println(shoppingCar);
       Boolean exists=redisUtil.hHasKey(CART_KEY_PREFIX+userId,goodId);
        if (exists) {
            String json = redisUtil.hget(CART_KEY_PREFIX + userId, goodId);
            System.out.println(json);
            if (json != null) {
                //转换为java实体类
                System.out.println("-------");
                ShoppingCarVo shoppingCarVo = JSON.toJavaObject(JSONObject.parseObject(json), ShoppingCarVo.class);
                System.out.println(shoppingCarVo);
                int num = shoppingCarVo.getShoppingCar().getGoodAmount() + shoppingCar.getGoodAmount();
               // System.out.println(num);
                ShoppingCar shoppingCarNew = shoppingCarVo.getShoppingCar();
                shoppingCarNew.setGoodAmount(num);
                shoppingCarVo.setShoppingCar(shoppingCarNew);
               // System.out.println(shoppingCarVo);
                redisTemplate.opsForHash().put(CART_KEY_PREFIX + userId, goodId, JSON.toJSONString(shoppingCarVo));
              redisTemplate.expire(CART_KEY_PREFIX + userId,7,TimeUnit.DAYS);
            }
        }
         int id=shoppingCar.getSecondHandId();
        Goods goods=goodsService.findGood(id);
        if (goods==null){
          return 0;}
        ShoppingCarVo shoppingCarVo=new ShoppingCarVo();
        shoppingCarVo.setShoppingCar(shoppingCar);
       String  cartImg=goods.getCartImg().get(0);
        shoppingCarVo.setImgUrl("http://"+cartImg);
        shoppingCarVo.setPrice(goods.getPrice());
        shoppingCarVo.setNum(shoppingCar.getGoodAmount());
        shoppingCarVo.setGoodName(goods.getCartTitle());
        redisUtil.hset(CART_KEY_PREFIX+userId,goodId,JSON.toJSONString(shoppingCarVo));
        redisTemplate.expire(CART_KEY_PREFIX+userId,7,TimeUnit.DAYS);
        return 1;
    }

    @Override
    public List<ShoppingCarVo> findAllProduct(Integer userId) {
        String userID= String.valueOf(userId);
       // System.out.println(userID);
        List<ShoppingCarVo> carVoList=new LinkedList<ShoppingCarVo>();
        //List jsonList =redisTemplate.opsForList().range("cart:"+userID,0,-1);
        List<String> jsonList = redisTemplate.opsForHash().values(CART_KEY_PREFIX+userID);
       // System.out.println(jsonList+"xxx");
        if (jsonList!=null){
            for (String json:jsonList){
                ShoppingCarVo shoppingCarVo=JSON.toJavaObject(JSONObject.parseObject(json),ShoppingCarVo.class);
              //  System.out.println(shoppingCarVo);
                carVoList.add(shoppingCarVo);
            }
        }
        return carVoList;
    }

    @Override
    public List<ShoppingCarVo> findProducts(Integer userId, List<String> goods) {
        String userID= String.valueOf(userId);
        List<ShoppingCarVo> carVoList=new LinkedList<ShoppingCarVo>();
        for (int i=0;i<goods.size();i++){
            String json= (String) redisTemplate.opsForHash().get(CART_KEY_PREFIX+userID,goods.get(i));
            if (json!=null){
                ShoppingCarVo shoppingCarVo=JSON.toJavaObject(JSONObject.parseObject(json),ShoppingCarVo.class);
                carVoList.add(shoppingCarVo);
            }
        }
        return carVoList;
    }

    @Override
    public int productAmountASC(Integer userId, Integer productId) {
        String userID = String.valueOf(userId);
        String productID = String.valueOf(productId);

        String json = redisUtil.hget(CART_KEY_PREFIX+userID,productID);
        if(json == null) {
            return 0;
        }
        ShoppingCarVo shoppingCarVo = JSON.toJavaObject(JSONObject.parseObject(json),ShoppingCarVo.class);
        int num = shoppingCarVo.getShoppingCar().getGoodAmount() + 1;
        ShoppingCar shoppingCar = shoppingCarVo.getShoppingCar();
        shoppingCar.setGoodAmount(num);
        shoppingCarVo.setShoppingCar(shoppingCar);
        redisUtil.hset(CART_KEY_PREFIX+userID,productID,JSON.toJSON(shoppingCarVo).toString());
        return 1;
    }

    @Override
    public int productAmountDESC(Integer userId, Integer productId) {

        String userID = String.valueOf(userId);
        String productID = String.valueOf(productId);
        String json = redisUtil.hget(CART_KEY_PREFIX+userID,productID);
        if(json == null) {
            return 0;
        }
        ShoppingCarVo shoppingCarVo = JSON.toJavaObject(JSONObject.parseObject(json),ShoppingCarVo.class);

        int num = shoppingCarVo.getShoppingCar().getGoodAmount() - 1;
        ShoppingCar shoppingCar = shoppingCarVo.getShoppingCar();
        shoppingCar.setGoodAmount(num);
        //System.out.println(num);
        if (num!=0){
            shoppingCarVo.setShoppingCar(shoppingCar);
            redisUtil.hset(CART_KEY_PREFIX+userID,productID,JSON.toJSON(shoppingCarVo).toString());

        }else {
            redisTemplate.opsForHash().delete(CART_KEY_PREFIX+userID,productID);
        }
        return 1;
    }

    @Override
    public int productAmount(Integer userId, Integer productId, Integer num) {
        String userID = String.valueOf(userId);
        String productID = String.valueOf(productId);
        String json = redisUtil.hget(CART_KEY_PREFIX+userID,productID);
        if(json == null) {
            return 0;
        }
        ShoppingCarVo shoppingCarVo = JSON.toJavaObject(JSONObject.parseObject(json),ShoppingCarVo.class);
         shoppingCarVo.setNum(num);
        ShoppingCar shoppingCar = shoppingCarVo.getShoppingCar();
        shoppingCar.setGoodAmount(num);
        //System.out.println(num);
        if (num!=0){
            shoppingCarVo.setShoppingCar(shoppingCar);
            redisUtil.hset(CART_KEY_PREFIX+userID,productID,JSON.toJSON(shoppingCarVo).toString());

        }else {
            redisTemplate.opsForHash().delete(CART_KEY_PREFIX+userID,productID);
        }
        return 1;
    }

    @Override
    public int delCartProduct(Integer userId, Integer productId) {
        String userID= String.valueOf(userId);
        String goodId= String.valueOf(productId);
        redisUtil.hdel(CART_KEY_PREFIX+userID,goodId);
        return 1;
    }
    // 判断商品是否存在于购物车中
    @Override
    public Boolean isProductInCart(String userId, String goodId) {
        return redisTemplate.opsForHash().hasKey(CART_KEY_PREFIX+userId,goodId);
    }

    @Override
    public boolean handleProductInGood(String goodId) {
        // 商品下架事件处理程序
        // 遍历购物车中的所有用户，检查购物车中的商品是否已下架，并进行相应的数据更新
        Set<String> userId=redisTemplate.keys(CART_KEY_PREFIX+"*");
      //  System.out.println("--------------");
            for (String u:userId) {
              //  System.out.println(Integer.valueOf(u)+"-----------------------------u");
               String cartId=u.replace(CART_KEY_PREFIX,"");
                if (isProductInCart(cartId,goodId)==true){
                  boolean exit=redisUtil.hHasKey(u, String.valueOf(goodId));
                    //System.out.println(exit);
                  if (exit){
                      int f=delCartProduct(Integer.valueOf(cartId), Integer.valueOf(goodId));
                      //System.out.println(f+"-----------------------------------------f");
                  }
                }
            }
        return true;
    }

    @Override
    public boolean handleProductInGoods(Goods goods) {
        Set<String> userId=redisTemplate.keys(CART_KEY_PREFIX+"*");
        ShoppingCar shoppingCar=new ShoppingCar();
        try {
            for (String u:userId) {
                String cartId=u.replace(CART_KEY_PREFIX,"");
                String goodId= String.valueOf(goods.getSecondHandId());
                if (isProductInCart(cartId,goodId)){
                    redisTemplate.opsForHash().delete(u,goodId);
                    System.out.println("000000000");
                    shoppingCar.setSecondHandId(goods.getSecondHandId());
                    shoppingCar.setGoodAmount(1);
                    shoppingCar.setUserId(Integer.valueOf(cartId));
                    UUID uuid=UUID.randomUUID();
                    shoppingCar.setCarId(uuid.toString().trim().replaceAll("-",""));
                    addCart(shoppingCar);
                }
            }
        }catch (Exception e){
            return false;
        }
        return  true;
    }
}
