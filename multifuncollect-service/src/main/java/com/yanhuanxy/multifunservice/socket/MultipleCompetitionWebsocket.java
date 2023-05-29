package com.yanhuanxy.multifunservice.socket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ServerEndpoint("/multipleMatch/{userId}/{sessionId}")
public class MultipleCompetitionWebsocket {

    private Session session;

    /**
     * 触发开启 websocket
     * 1、主动匹配
     * 2、用户重连
     * @param session
     * @param userId
     * @param sessionId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value="userId")Integer userId, @PathParam(value="sessionId")String sessionId) {
        this.session = session;
        CurrentCachePool.webSockets.put(userId,this);
        List<Object> list = new ArrayList<>();
        list.add(sessionId);
        list.add(session);
        CurrentCachePool.sessionPool.put(userId , list);
        System.out.println("【websocket消息】有新的连接，总数为:"+ CurrentCachePool.webSockets.size());
    }


    /**
     * 触发关闭websocket
     * 1、用户主动取消
     * 2、用户掉线 超出本场答题时间 房间关闭
     */
    @OnClose
    public void onClose(Session session) {
        // 断开连接删除用户删除session
        Integer userId = Integer.parseInt(this.session.getRequestParameterMap().get("userId").get(0));
        CurrentCachePool.sessionPool.remove(userId);
        CurrentCachePool.webSockets.remove(userId);

        CurrentCachePool.curUserPool.remove("用户id");
        System.out.println("【websocket消息】连接断开，总数为:" + CurrentCachePool.webSockets.size());
    }

    /**
     * 触发发送信息
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {
        System.out.println(message);
    }
}
