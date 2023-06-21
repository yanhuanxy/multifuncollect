package com.yanhuanxy.multifunweb.config;


import com.yanhuanxy.multifunservice.socket.MultipleCompetitionWebsocket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 原生 websocket 实现 websocket
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig {

    @Bean
    public MultipleCompetitionWebsocket reverseWebSocketEndpoint() {

        return new MultipleCompetitionWebsocket();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){

        return new ServerEndpointExporter();
    }
}
