package wtt.vo;

import lombok.Data;
import wtt.pojo.ShoppingCar;

import java.io.Serializable;
import java.math.BigDecimal;

//用于前端页面用户看到的购物车信息
@Data
public class ShoppingCarVo implements Serializable {
    private static final long serialVersionUID = 2616783859683578863L;
    private String goodName;
    private String imgUrl;
    private BigDecimal price;
    private Integer num;
    private ShoppingCar shoppingCar;
}
