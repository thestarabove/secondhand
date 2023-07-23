package wtt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import wtt.vo.UserVo;

/**
 * tb_order
 * @汪涛涛
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@TableName("tb_order")
public class Order implements Serializable {
    private Long orderId;

    private Integer secondHandId;

    private Integer userId;
    private  Integer sellerId;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;

    private String state;

    private String total;
    @TableField(select = false)
    private String num;
   // @TableField(select = false)
    private Goods goods;

    private UserVo userVo;

    private static final long serialVersionUID = 1L;
}