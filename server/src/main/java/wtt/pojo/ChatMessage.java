package wtt.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("tb_chat_message")
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @TableId(value = "message_id", type = IdType.AUTO)
    private Long messageId;
    private Long sessionId;
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private Integer isRead;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}
