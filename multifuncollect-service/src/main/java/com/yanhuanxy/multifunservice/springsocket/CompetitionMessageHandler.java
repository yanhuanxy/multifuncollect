package com.yanhuanxy.multifunservice.springsocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.text.MessageFormat;
import java.util.ArrayDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Component
public class CompetitionMessageHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompetitionMessageHandler.class);

    /**
     *解析消息线程池
     **/
    private final ScheduledExecutorService executorReceive;

    /**
     *发送消息线程池
     **/
    private final ScheduledExecutorService executorSend;

    public CompetitionMessageHandler() {
        //构造方法初始化线程池
        this.executorReceive = new ScheduledThreadPoolExecutor(1);
        this.executorSend = new ScheduledThreadPoolExecutor(1);
    }

    /**
     * 待解析的消息队列
     */
    public static ArrayDeque<CompetitionMessageQueue> receiveQueue = new ArrayDeque<CompetitionMessageQueue>();

    /**
     * 待发送的消息队列
     */
    public static ArrayDeque<CompetitionMessageQueue> sendQueue = new ArrayDeque<CompetitionMessageQueue>();


    /**
     * 建立成功事件
     * @param session session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    }

    @Override
    public void handleMessage(WebSocketSession wsSession, WebSocketMessage<?> message) throws Exception {
        LOGGER.info("message =>"  + message);
        //放人 待解析消息到  队列
        CompetitionMessagePutQueue.PutMessageToQueue(wsSession, message, 1);
        //启动解析消息线程
        executorReceive.execute(receiveRunnable);
    }


    /**
     * 发送信息
     * @param wsSession session
     * @param message 信息
     */
    public void handleSendMessage(WebSocketSession wsSession, String message){
        //放人 待发送消息到  队列
        CompetitionMessagePutQueue.PutMessageToQueue(wsSession, message, 2);
        //启动发送消息线程
        executorSend.execute(sendRunnable);
    }

    /**
     * 调用父类 信息处理方法
     * @param session session
     * @param message 消息
     */
    public void handleMessageParent(WebSocketSession session, WebSocketMessage<?> message) {
        try {
            super.handleMessage(session, message);
        }catch (Exception e){
            LOGGER.error("字符串信息处理异常", e);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        //组装返回的Echo信息

//        String echoMessage = this.echoService.echo(message.getPayload());

        LOGGER.debug(MessageFormat.format("Echo message \"{0}\"", message));

        session.sendMessage(new TextMessage(message.asBytes()));

    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

        session.close(CloseStatus.SERVER_ERROR);

        LOGGER.debug("Info: WebSocket connection closed.");
    }


    /**
     * 解析消息线程，负责从 待解析的消息队列receiveQueue中 取出消息，然后进行解析
     */
    private final Runnable receiveRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                //从队列中取消息
                CompetitionMessageQueue webSocketMsg = receiveQueue.poll();
                if (webSocketMsg != null) {
                    LOGGER.info("webSocketMsg.getMessage =>"  + webSocketMsg.getMessage());
                    // 重新利用父类方法
                    handleMessageParent(webSocketMsg.getSession(),  webSocketMsg.getMessage());
                } else {
                    //如果取不到消息时就跳出while (true)循环，避免无必要的消耗cpu
                    break;
                }
            }
        }
    };

    /**
     * 发送消息线程，负责从 待发送的消息队列sendQueue中 取出消息，然后进行发送
     */
    private static final Runnable sendRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                //从队列中取消息
                CompetitionMessageQueue webSocketMsg = sendQueue.poll();
                if (webSocketMsg != null) {
                    try {
                        LOGGER.info("执行发送消息");
                        webSocketMsg.send();
                    } catch (Exception e) {
                        LOGGER.error("处理client的消息异常 onMessage error :{}", e.getMessage(), e);
                    }
                } else {
                    //如果取不到消息时就跳出while (true)循环，避免无必要的消耗cpu
                    break;
                }
            }
        }
    };
}
