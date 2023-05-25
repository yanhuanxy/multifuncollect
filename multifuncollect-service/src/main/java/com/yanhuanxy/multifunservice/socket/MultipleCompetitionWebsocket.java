package com.yanhuanxy.multifunservice.socket;

import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.util.ArrayList;
import java.util.List;

@ServerEndpoint("/multipleMatch/{userId}/{sessionId}")
public class MultipleCompetitionWebsocket {

    private Session session;

    @OnOpen
    public void onOpen(Session session, @PathParam(value="userId")Integer userId, @PathParam(value="sessionId")String sessionId) {
        this.session = session;
        CurrentCachePool.webSockets.put(userId,this);
        List<Object> list = new ArrayList<>();
        list.add(sessionId);
        list.add(session);
        CurrentCachePool.sessionPool.put(userId , list);
        System.out.println("【websocket消息】有新的连接，总数为:"+CurrentCachePool.webSockets.size());
    }
}
