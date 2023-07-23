package wtt.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wtt.pojo.User;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@TableName("tb_goods")
@AllArgsConstructor
@NoArgsConstructor
public class GoodVo {
    @TableId(value = "second_hand_id",type = IdType.AUTO)
    private Integer secondHandId;
    private String cartTitle;

    private String cartImg;
    private String cartDescription;
    @TableField(value ="create_time" ,fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    private BigDecimal price;
    private String status;

    private Integer userId;

    @TableField(select = false)
    private User user;

}
