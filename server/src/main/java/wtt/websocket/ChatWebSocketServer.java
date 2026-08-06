package wtt.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketServer extends TextWebSocketHandler {
    private static final Map<Integer, WebSocketSession> SESSION_MAP = new ConcurrentHashMap<>();

    /**
     * 推送给指定用户（在线则实时下发）
     */
    public static void pushToUser(Integer userId, Object data) {
        WebSocketSession session = SESSION_MAP.get(userId);
        if (session == null || !session.isOpen()) {
            return;
        }
        try {
            JSONObject json = new JSONObject();
            json.put("type", "CHAT");
            json.put("data", data);
            session.sendMessage(new TextMessage(JSON.toJSONString(json)));
        } catch (IOException e) {
            e.printStackTrace();
            SESSION_MAP.remove(userId);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Integer userId = (Integer) session.getAttributes().get("userId");
        if (userId == null) {
            try {
                session.close(CloseStatus.NOT_ACCEPTABLE);
            } catch (IOException ignored) {
            }
            return;
        }
        SESSION_MAP.put(userId, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Integer userId = (Integer) session.getAttributes().get("userId");
        if (userId != null) {
            SESSION_MAP.remove(userId, session);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        Integer userId = (Integer) session.getAttributes().get("userId");
        if (userId != null) {
            SESSION_MAP.remove(userId, session);
        }
    }
}
