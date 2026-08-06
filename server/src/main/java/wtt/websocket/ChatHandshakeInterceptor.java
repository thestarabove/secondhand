package wtt.websocket;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class ChatHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String token = getToken(request);
        if (token == null) {
            return false;
        }
        Object loginId = StpUtil.getLoginIdByToken(token);
        if (loginId == null) {
            return false;
        }
        attributes.put("userId", Integer.valueOf(loginId.toString()));
        return true;
    }

    private String getToken(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            String query = ((ServletServerHttpRequest) request).getServletRequest().getQueryString();
            if (query != null) {
                for (String param : query.split("&")) {
                    String[] kv = param.split("=", 2);
                    if (kv.length == 2 && "token".equals(kv[0])) {
                        return kv[1];
                    }
                }
            }
            String cookie = request.getHeaders().getFirst(HttpHeaders.COOKIE);
            if (cookie != null) {
                for (String item : cookie.split(";")) {
                    String[] kv = item.trim().split("=", 2);
                    if (kv.length == 2 && "satoken".equals(kv[0])) {
                        return kv[1];
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
