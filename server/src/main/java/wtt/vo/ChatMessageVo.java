package wtt.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessageVo {
    private Long messageId;
    private Long sessionId;
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private Integer isRead;
    private Date createTime;
}
