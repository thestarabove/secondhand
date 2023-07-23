package wtt.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import wtt.vo.UserVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName("tb_goods")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Goods {
    @TableId(value = "second_hand_id",type = IdType.AUTO)
    private Integer secondHandId;
    private String cartTitle;

    private List<String> cartImg;
    private String cartDescription;
    @TableField(value ="create_time" ,fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;
    private BigDecimal price;
    private String status;

    private Integer userId;
    private Integer goodNum;
    @TableField(select = false)
    private UserVo userVo;


}
