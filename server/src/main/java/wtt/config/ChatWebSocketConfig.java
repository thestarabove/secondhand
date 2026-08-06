package wtt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import wtt.websocket.ChatHandshakeInterceptor;
import wtt.websocket.ChatWebSocketServer;

@Configuration
@EnableWebSocket
public class ChatWebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private ChatWebSocketServer chatWebSocketServer;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketServer, "/ws/chat")
                .addInterceptors(new ChatHandshakeInterceptor())
                .setAllowedOriginPatterns("*");
    }
}
