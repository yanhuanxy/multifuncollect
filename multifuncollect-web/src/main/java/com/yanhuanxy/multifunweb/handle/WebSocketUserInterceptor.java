package com.yanhuanxy.multifunweb.handle;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.Map;

/**
 * @author yym
 */
public class WebSocketUserInterceptor implements ChannelInterceptor {

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {

        System.out.println(sent);
        ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);

    }


    /**
     * 获取包含在stomp中的用户信息
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println(message);
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null) {
            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
                if (raw instanceof Map) {
                    Object nameObj = ((Map) raw).get("name");
                    if (nameObj != null) {
                        // 设置当前访问器的认证用户,或者做其他业务
//                        WebSocketUser webSocketUser = new WebSocketUser(String.valueOf(nameObj));
//                        accessor.setUser(webSocketUser);
                    }
                }
            }
        }
        return message;
    }
}
