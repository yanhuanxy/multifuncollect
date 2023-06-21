package com.yanhuanxy.multifunservice.springsocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * 统一的 put 消息到 队列 的公共类  对外提供唯一的方法
 **/
@Component
public class CompetitionMessagePutQueue {

    /**
     * @param session   socket链接
     * @param message       消息
     * @param type      1  放人 receiveQueue (待解析消息)队列    2  放入  sendQueue （待发送消息） 队列   注意  不要放错队列！
     */
    public static void PutMessageToQueue(WebSocketSession session , WebSocketMessage<?> message, int type){
        CompetitionMessageQueue queue = new CompetitionMessageQueue();
        queue.setSession(session);
        queue.setMessage(message);
        if (type == 1){
            //将消息放入 待解析的消息 队列
            CompetitionMessageHandler.receiveQueue.add(queue);
        }else if (type == 2){
            //将消息放入 待发送的消息 队列
            CompetitionMessageHandler.sendQueue.add(queue);
        }
    }

    /**
     * @param session socket链接
     * @param message 消息
     * @param type 1  放人 receiveQueue (待解析消息)队列    2  放入  sendQueue （待发送消息） 队列   注意  不要放错队列！
     */
    public static void PutMessageToQueue(WebSocketSession session , String message, int type){
        PutMessageToQueue(session, new TextMessage(message), type);
    }
}
