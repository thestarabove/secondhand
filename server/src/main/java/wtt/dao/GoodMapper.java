package wtt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import wtt.pojo.Goods;
import wtt.utils.ListToVarcharTypeHandler;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodMapper extends BaseMapper<Goods> {
    public boolean addGood(Goods goods);
    public Goods findGoodById(Integer secondHandId);
    public List<Goods> findGoodsByUserId(Integer userId ,String status);
    public boolean deleteBySecondHandId(Integer secondHandId);
    public boolean deleteBySellerId(Integer sellerId);
    public Goods updateCartImgByUserIdGoods(String img,Integer secondHandId);
    public boolean updateSecondHandId(Goods goods);
    public List<Goods> findAllGoodByPage(Map<String,Object> map);

    @Select("SELECT second_hand_id, cart_title, cart_img, cart_description, create_time, update_time, price, status, user_id FROM tb_goods where  second_hand_id IN" +
            " (SELECT second_hand_id FROM tb_goods WHERE status = #{status} ORDER BY create_time DESC) LIMIT #{startIndex}, 4;")
    @Results(id = "goodsResultMap", value = {
            @Result(property = "secondHandId", column = "second_hand_id"),
            @Result(property = "cartTitle", column = "cart_title"),
            @Result(property = "cartImg", column = "cart_img", javaType = List.class, jdbcType = JdbcType.VARCHAR, typeHandler = ListToVarcharTypeHandler.class),
            @Result(property = "cartDescription", column = "cart_description"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "price", column = "price"),
            @Result(property = "goodNum",column = "good_num"),
            @Result(property = "status", column = "status"),
            @Result(property = "userId", column = "user_id")
    })
    List<Goods> findAllGoodsByPage(@Param("status") String status, @Param("startIndex") Integer startIndex);


    @Select("SELECT second_hand_id, cart_title, cart_img, cart_description, create_time, update_time, price, status, user_id FROM tb_goods WHERE status=#{status} AND cart_title LIKE CONCAT(#{goodName}, '%') ORDER BY create_time DESC ")
    @Results(id = "goodsNameResultMap", value = {
            @Result(property = "secondHandId", column = "second_hand_id"),
            @Result(property = "cartTitle", column = "cart_title"),
            @Result(property = "cartImg", column = "cart_img", javaType = List.class, jdbcType = JdbcType.VARCHAR, typeHandler = ListToVarcharTypeHandler.class),
            @Result(property = "cartDescription", column = "cart_description"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "price", column = "price"),
            @Result(property = "status", column = "status"),
            @Result(property = "userId", column = "user_id")
    })
    List<Goods> findGoodsByNamePage(@Param("status") String status, @Param("goodName") String goodName);






    List<Goods> findAllGoodsBySeller(@Param("userId") Integer userId,@Param("status") String status, @Param("startIndex") Integer startIndex);




}
