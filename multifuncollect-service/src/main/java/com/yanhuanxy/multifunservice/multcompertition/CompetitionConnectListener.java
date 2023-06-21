package com.yanhuanxy.multifunservice.multcompertition;

import com.yanhuanxy.multifuncommon.base.vo.ResponseVO;
import com.yanhuanxy.multifuncommon.enums.multcompertition.MessageTypeEnums;
import com.yanhuanxy.multifuncommon.exception.BaseRuntimeException;
import com.yanhuanxy.multifundomain.system.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;

/**
 * 用户添加队列
 * 1、已存在 告诉前端 调用重入房间方法
 * 2、不存在 进入等待队列 调用匹配方法
 */
@Component
public class CompetitionConnectListener implements ApplicationListener<SessionConnectEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompetitionConnectListener.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        Principal principal = event.getUser();
        if (principal != null){
            try{
                ResponseVO<Object> responseVO = new ResponseVO<Object>();
                // 获取用户信息
                Message<byte[]> socketMessage = event.getMessage();
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(socketMessage, StompHeaderAccessor.class);
                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    Object sessionAttributes = socketMessage.getHeaders().get(SimpMessageHeaderAccessor.SESSION_ATTRIBUTES);
                    if (sessionAttributes instanceof Map) {
                        Object userInfo = ((Map<?, ?>) sessionAttributes).get("userInfo");
                        if(Objects.isNull(userInfo)){
                            throw new BaseRuntimeException("用户认证失败！获取用户信息为空");
                        }
                        SysUser tmpSysUser = (SysUser) userInfo;
                        LOGGER.info("client name: {} connect.....", tmpSysUser.getNickname());
                        // 将用户添加到等待队列
                        boolean hasUser = CurrentCachePool.curUserPool.containsKey(tmpSysUser.getId());
                        String message;
                        if(hasUser){
                            message = "重新进入！";
                            responseVO.setCode(MessageTypeEnums.RE_ADD_USER.getCode().toString());
                        }else{
                            message = "等待队友";
                            responseVO.setCode(MessageTypeEnums.ADD_USER.getCode().toString());
                            CurrentCachePool.curUserPool.put(tmpSysUser.getId(), tmpSysUser);
                        }
                        responseVO.setMessage(message);
                        simpMessagingTemplate.convertAndSendToUser(String.valueOf(tmpSysUser.getId()),"/goTo/multiple.start", responseVO);
                    }
                }
            }catch (Exception e){
                LOGGER.error("【websocket 建立连接】用户进入等待队列", e);
            }
        }
    }
}
