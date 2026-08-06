package wtt.service;

import wtt.vo.ChatMessageVo;
import wtt.vo.ChatSessionVo;

import java.util.List;

public interface ChatService {
    ChatSessionVo createOrGetSession(Integer currentUserId, Integer peerUserId, Integer goodId);

    List<ChatSessionVo> getSessionList(Integer userId);

    List<ChatMessageVo> getMessages(Long sessionId, Integer userId);

    ChatMessageVo sendMessage(Long sessionId, Integer senderId, Integer receiverId, String content);
}
