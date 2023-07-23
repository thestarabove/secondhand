package wtt.pojo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * tb_ratingform
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@TableName("tb_ratingform")
public class Ratingform implements Serializable {
    @TableId(value = "evaluate_id",type = IdType.AUTO)
    private Integer evaluateId;

    private Long orderId;

    private String state;

    private String starRating;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private String content;

    private Integer userId;
    @TableField(select = false)
    private Order order;

    private static final long serialVersionUID = 1L;
}