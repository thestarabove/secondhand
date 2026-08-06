package wtt.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wtt.service.ChatService;
import wtt.utils.ComFoundException;
import wtt.utils.RedisUtil;
import wtt.utils.ResponseJsonStatus;
import wtt.vo.ChatMessageVo;
import wtt.vo.ChatSessionVo;
import wtt.vo.ResponseJsonMessage;

import java.util.List;

@RestController
@RequestMapping("/secondhand/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private RedisUtil redisUtil;

    private Integer currentUserId() {
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        Object userId = saTokenInfo == null ? null : redisUtil.get("token:" + saTokenInfo.getTokenValue());
        if (userId == null) {
            throw new ComFoundException("请先登录");
        }
        return Integer.valueOf(userId.toString());
    }

    /**
     * 创建或获取与对方的会话
     */
    @GetMapping("/session")
    public ResponseJsonMessage createOrGetSession(@RequestParam("peerUserId") Integer peerUserId,
                                                  @RequestParam(value = "goodId", required = false) Integer goodId) {
        ChatSessionVo vo = chatService.createOrGetSession(currentUserId(), peerUserId, goodId);
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(),
                ResponseJsonStatus.SUCCESS.getMessage(), vo);
    }

    /**
     * 我的会话列表
     */
    @GetMapping("/sessionList")
    public ResponseJsonMessage sessionList() {
        List<ChatSessionVo> list = chatService.getSessionList(currentUserId());
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(),
                ResponseJsonStatus.SUCCESS.getMessage(), list);
    }

    /**
     * 会话历史消息（读取时自动标记已读）
     */
    @GetMapping("/messages")
    public ResponseJsonMessage messages(@RequestParam("sessionId") Long sessionId) {
        List<ChatMessageVo> list = chatService.getMessages(sessionId, currentUserId());
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(),
                ResponseJsonStatus.SUCCESS.getMessage(), list);
    }

    /**
     * 发送消息
     */
    @PostMapping("/send")
    public ResponseJsonMessage send(@RequestBody JSONObject body) {
        Long sessionId = body.getLong("sessionId");
        Integer receiverId = body.getInteger("receiverId");
        String content = body.getString("content");
        ChatMessageVo vo = chatService.sendMessage(sessionId, currentUserId(), receiverId, content);
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(),
                ResponseJsonStatus.SUCCESS.getMessage(), vo);
    }
}
