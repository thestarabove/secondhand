package wtt.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * tb_likes
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@TableName("tb_likes")
public class Likes implements Serializable {
    @TableId(value = "like_id",type = IdType.AUTO)
    private Integer likeId;

    private Integer secondHandId;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Integer userId;
    @TableField(select = false)
    private Goods goods;
    private static final long serialVersionUID = 1L;
}