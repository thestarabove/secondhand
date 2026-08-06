package wtt.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ChatSessionVo {
    private Long sessionId;
    private Integer peerUserId;
    private String peerUserName;
    private String peerUserImg;
    private String lastMessage;
    private Date lastMessageTime;
    private Integer unreadCount;
    private Integer goodId;
    private String goodTitle;
}
