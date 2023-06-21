package com.yanhuanxy.multifunweb.config;


import com.yanhuanxy.multifunservice.springsocket.CompetitionMessageHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketSpringConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(new CompetitionMessageHandler(), "/echoMessage");
        registry.addHandler(new CompetitionMessageHandler(), "/echoMessage_SockJs" ).withSockJS();
    }
}
