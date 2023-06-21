package com.yanhuanxy.multifunservice.springsocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * 消息发送
 **/
@Component
public class CompetitionMessageQueue {
    /**
     *socket会话信息
     */
    private WebSocketSession session;
    /**
     * 消息
     */
    private WebSocketMessage<?> message;

    /**
     * 统一的发送消息方法
     */
    public void send() throws IOException {
        if(session != null && session.isOpen()){
            session.sendMessage(message);
        }
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public WebSocketMessage<?> getMessage() {
        return message;
    }

    public void setMessage(WebSocketMessage<?> message) {
        this.message = message;
    }
}
