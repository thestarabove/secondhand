package wtt.service;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import wtt.pojo.Goods;
import wtt.pojo.ShoppingCar;
import wtt.vo.ShoppingCarVo;

import java.util.List;

public interface CartService {
    //@Insert("insert into cms_shopcar(id,user_id,product_id,product_amount,seller_id) values(#{id},#{userId},#{productId},#{productAmount},#{sellerId})")
    public int addCart(ShoppingCar shoppingCar);

   // @Select("select * from cms_shopcar where user_id=#{userId}")
    public List<ShoppingCarVo> findAllProduct(Integer userId);

    public List<ShoppingCarVo> findProducts(Integer userId,List<String> goods);

    // @Update("update cms_shopcar set product_amount = product_amount + 1 where user_id=#{userId} and product_id=#{productId}")
    public int productAmountASC(Integer userId, Integer productId);
   // @Update("update cms_shopcar set product_amount = product_amount - 1 where user_id=#{userId} and product_id=#{productId}")
    public int productAmountDESC(Integer userId, Integer productId);
    public int productAmount(Integer userId, Integer productId,Integer num);

  //  @Delete("delete from cms_shopcar where user_id=#{userId} and product_id=#{productId}")
    public int delCartProduct(Integer userId, Integer productId);

      Boolean isProductInCart(String userId,String goodId);
      boolean handleProductInGood(String goodId);

    boolean handleProductInGoods(Goods goods);

}
