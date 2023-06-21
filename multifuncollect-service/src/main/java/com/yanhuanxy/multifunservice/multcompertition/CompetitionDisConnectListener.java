package com.yanhuanxy.multifunservice.multcompertition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

/**
 * 触发关闭websocket
 * 用户主动取消
 */
@Component
public class CompetitionDisConnectListener implements ApplicationListener<SessionDisconnectEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompetitionDisConnectListener.class);

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        Principal principal = event.getUser();
        if (principal != null){
            try{
                //干掉 离线的
                CurrentCachePool.curUserPool.remove(Long.valueOf(principal.getName()));
                LOGGER.info("【websocket消息】连接断开，总数为:{}", CurrentCachePool.curUserPool.size());
            }catch (Exception e){
                LOGGER.error("【websocket关闭连接】异常", e);
            }
        }
    }
}
