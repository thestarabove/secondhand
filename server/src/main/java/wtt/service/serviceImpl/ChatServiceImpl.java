package wtt.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import wtt.dao.ChatMessageMapper;
import wtt.dao.ChatSessionMapper;
import wtt.dao.GoodMapper;
import wtt.dao.UserDao;
import wtt.pojo.ChatMessage;
import wtt.pojo.ChatSession;
import wtt.pojo.Goods;
import wtt.pojo.User;
import wtt.service.ChatService;
import wtt.utils.ComFoundException;
import wtt.vo.ChatMessageVo;
import wtt.vo.ChatSessionVo;
import wtt.websocket.ChatWebSocketServer;

import java.util.Date;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatSessionMapper chatSessionMapper;
    @Autowired
    private ChatMessageMapper chatMessageMapper;
    @Autowired
    private GoodMapper goodMapper;
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public ChatSessionVo createOrGetSession(Integer currentUserId, Integer peerUserId, Integer goodId) {
        if (currentUserId == null || peerUserId == null || currentUserId.equals(peerUserId)) {
            throw new ComFoundException("不能与自己聊天");
        }
        Integer buyerId = currentUserId;
        Integer sellerId = peerUserId;
        if (goodId != null) {
            Goods goods = goodMapper.selectById(goodId);
            if (goods != null && goods.getUserId() != null) {
                if (!currentUserId.equals(goods.getUserId())) {
                    // 非商品主人发起，卖家固定为商品主人
                    sellerId = goods.getUserId();
                } else {
                    // 商品主人主动发起，角色对调
                    buyerId = peerUserId;
                    sellerId = currentUserId;
                }
            }
        }
        final Integer fBuyerId = buyerId;
        final Integer fSellerId = sellerId;
        ChatSession session = chatSessionMapper.selectOne(new LambdaQueryWrapper<ChatSession>()
                .eq(goodId != null, ChatSession::getGoodId, goodId)
                .and(w -> w.eq(ChatSession::getBuyerId, fBuyerId).eq(ChatSession::getSellerId, fSellerId)
                        .or().eq(ChatSession::getBuyerId, fSellerId).eq(ChatSession::getSellerId, fBuyerId))
                .orderByDesc(ChatSession::getUpdateTime)
                .last("limit 1"));
        if (session == null) {
            session = new ChatSession();
            session.setBuyerId(buyerId);
            session.setSellerId(sellerId);
            session.setGoodId(goodId);
            Date now = new Date();
            session.setCreateTime(now);
            session.setUpdateTime(now);
            chatSessionMapper.insert(session);
        }
        return buildSessionVo(session, currentUserId);
    }

    @Override
    public List<ChatSessionVo> getSessionList(Integer userId) {
        return chatSessionMapper.selectSessionList(userId);
    }

    @Override
    @Transactional
    public List<ChatMessageVo> getMessages(Long sessionId, Integer userId) {
        ChatSession session = chatSessionMapper.selectById(sessionId);
        if (session == null) {
            throw new ComFoundException("会话不存在");
        }
        boolean participant = session.getBuyerId().equals(userId) || session.getSellerId().equals(userId);
        if (!participant) {
            throw new ComFoundException("无权查看该会话");
        }
        chatMessageMapper.updateReadBySession(sessionId, userId);
        return chatMessageMapper.selectMessagesBySession(sessionId);
    }

    @Override
    @Transactional
    public ChatMessageVo sendMessage(Long sessionId, Integer senderId, Integer receiverId, String content) {
        if (!StringUtils.hasText(content)) {
            throw new ComFoundException("消息内容不能为空");
        }
        ChatSession session = chatSessionMapper.selectById(sessionId);
        if (session == null) {
            throw new ComFoundException("会话不存在");
        }
        boolean participant = (session.getBuyerId().equals(senderId) && session.getSellerId().equals(receiverId))
                || (session.getSellerId().equals(senderId) && session.getBuyerId().equals(receiverId));
        if (!participant) {
            throw new ComFoundException("无权在该会话发送消息");
        }
        ChatMessage message = new ChatMessage();
        message.setSessionId(sessionId);
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setIsRead(0);
        chatMessageMapper.insert(message);

        ChatSession update = new ChatSession();
        update.setSessionId(sessionId);
        update.setUpdateTime(new Date());
        chatSessionMapper.updateById(update);

        ChatMessageVo vo = new ChatMessageVo();
        vo.setMessageId(message.getMessageId());
        vo.setSessionId(message.getSessionId());
        vo.setSenderId(message.getSenderId());
        vo.setReceiverId(message.getReceiverId());
        vo.setContent(message.getContent());
        vo.setIsRead(message.getIsRead());
        vo.setCreateTime(message.getCreateTime());
        // 实时推送给接收方
        ChatWebSocketServer.pushToUser(receiverId, vo);
        return vo;
    }

    private ChatSessionVo buildSessionVo(ChatSession session, Integer currentUserId) {
        ChatSessionVo vo = new ChatSessionVo();
        vo.setSessionId(session.getSessionId());
        vo.setGoodId(session.getGoodId());
        Integer peerUserId = session.getBuyerId().equals(currentUserId) ? session.getSellerId() : session.getBuyerId();
        vo.setPeerUserId(peerUserId);
        User peer = userDao.selectById(peerUserId);
        if (peer != null) {
            vo.setPeerUserName(peer.getUserName());
            vo.setPeerUserImg(peer.getImg());
        }
        if (session.getGoodId() != null) {
            Goods goods = goodMapper.selectById(session.getGoodId());
            if (goods != null) {
                vo.setGoodTitle(goods.getCartTitle());
            }
        }
        ChatMessage last = chatMessageMapper.selectOne(new LambdaQueryWrapper<ChatMessage>()
                .eq(ChatMessage::getSessionId, session.getSessionId())
                .orderByDesc(ChatMessage::getMessageId)
                .last("limit 1"));
        if (last != null) {
            vo.setLastMessage(last.getContent());
            vo.setLastMessageTime(last.getCreateTime());
        }
        Long unread = chatMessageMapper.selectCount(new LambdaQueryWrapper<ChatMessage>()
                .eq(ChatMessage::getSessionId, session.getSessionId())
                .eq(ChatMessage::getReceiverId, currentUserId)
                .eq(ChatMessage::getIsRead, 0));
        vo.setUnreadCount(unread == null ? 0 : unread.intValue());
        return vo;
    }
}
