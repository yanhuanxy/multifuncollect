package com.yanhuanxy.multifunservice.socket.test;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class StompClient {
    private static volatile Object object = new Object();

    public static void main(String[] args) {
        Map<Integer, String> head = new HashMap<>();
//        head.put(6, "eyJhbGciOiJIUzUxMiJ9.eyJMT0dJTl9VU0VSX0tFWV8iOiI1ZjBlNDc3Mi03NjUxLTQzYTMtYTlhYy1jMjkwMTRkZDE5MjIiLCJjbGFpbXNfY291bnRfa2V5IjoidGVzdDQiLCJpc3MiOiJrbm93bGVkZ2UiLCJzdWIiOiJVU0VSTkFNRSIsImF1ZCI6IldlYiIsImlhdCI6MTY4NjAxOTk0OX0.zjiCCLuYO0LDdy9AdVd7GvFalalQWLIh2D_WYVMeGRDFBal1LFEZoKG8AxKLnoRnEfycYG2A8IuQERLoSh7EUA");
        head.put(7, "eyJhbGciOiJIUzUxMiJ9.eyJMT0dJTl9VU0VSX0tFWV8iOiI2ZjE2ZDNiYS05MTgyLTRjOTUtYjVkYi01MTYzN2JmODhkMDQiLCJjbGFpbXNfY291bnRfa2V5IjoidGVzdDUiLCJpc3MiOiJrbm93bGVkZ2UiLCJzdWIiOiJVU0VSTkFNRSIsImF1ZCI6IldlYiIsImlhdCI6MTY4NjU2MjM0MH0.aPX0ejQj2co2k5_7Y34oAEpQcFSGX2bR89js80xKEtUo-9YVuwBz8RmgxNdnAz8mVFp1RcTCRSRJ5YvpSudn9Q");
//        creatThread(1, 6, head);

        Map<Integer, String> head2 = new HashMap<>();
        head2.put(10, "eyJhbGciOiJIUzUxMiJ9.eyJMT0dJTl9VU0VSX0tFWV8iOiJmZDgwODEzYy1iOTM2LTQyZTAtYmRkMy03MzIzZTgyMGViMGMiLCJjbGFpbXNfY291bnRfa2V5IjoidGVzdDgiLCJpc3MiOiJrbm93bGVkZ2UiLCJzdWIiOiJVU0VSTkFNRSIsImF1ZCI6IldlYiIsImlhdCI6MTY4NjAyMDA4Mn0.zzsxn5bLWNTFsv5A1dMPpr1Ol2q7bdbAOfnNPUFiAHzfZxKTNG1DxxlWT1zOBRMnlUJPGzl4GVuLLu1d2bwfcQ");
//        head2.put(11, "eyJhbGciOiJIUzUxMiJ9.eyJMT0dJTl9VU0VSX0tFWV8iOiJjZWRhZTg2ZS0yNzhiLTQwNGYtOGQ5MS03OWQ3OGZjMmY3MjUiLCJjbGFpbXNfY291bnRfa2V5IjoidGVzdDkiLCJpc3MiOiJrbm93bGVkZ2UiLCJzdWIiOiJVU0VSTkFNRSIsImF1ZCI6IldlYiIsImlhdCI6MTY4NjAyMDEwMX0.ErtMg-BIUvEbSnA5WzmNGWIDY8BjrJoOQTYcluEF4GKcm9Vw3svmJBiE62R2P8gKz1jvHjTLtaqkwUKhiImAPg");
//        creatThread(1, 10,head2);
        websocketClient.creatThread(1, 7, head);
    }

    public static void creatThread(int num, int start, Map<Integer, String> head){
        for (int i = start; i < start + num; i++) {
            int finalI = i;

            new Thread(()->{
                try {
                    StompClient stompClient = new StompClient();
                    final CountDownLatch latch = new CountDownLatch(1);
                    StompSession stompSession = stompClient.stompClient(finalI, head.get(finalI));
                    if(stompSession != null){
                        stompClient.sendMessage(finalI, stompSession);
                        latch.await(10, TimeUnit.SECONDS);
//                        stompSession.disconnect();
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * 开启连接
     */
    private StompSession stompClient(int index, String authorization){

        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient transport = new SockJsClient(transports);
        transport.setMessageCodec(new Jackson2SockJsMessageCodec());
        WebSocketStompClient stompClient = new WebSocketStompClient(transport);
        //接收大小限制
        stompClient.setInboundMessageSizeLimit(1024 * 1024);
        //处理心跳
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.afterPropertiesSet();
        //for heartbeats
        stompClient.setTaskScheduler(taskScheduler);
        StompSessionHandler customHandler = new CustomStompSessionHandler(index);
        //可以发送请求头
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("test1", authorization);
        stompHeaders.add("test2", authorization);
        URI uri = URI.create("http://192.168.0.98:8888/knowledge/ws");
        //阻塞
        try {
            WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
//            webSocketHttpHeaders.add("Authorization", authorization );

            CompletableFuture<StompSession> stompSessionCompletableFuture = stompClient.connectAsync(uri, webSocketHttpHeaders, stompHeaders, customHandler);
            StompSession stompSession = stompSessionCompletableFuture.get();
//            stompSession.send("/app/multiple.addUser/"+ index, messagestr.getBytes(StandardCharsets.UTF_8));
            return stompSession;
            //latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendMessage(int index, StompSession stompSession){
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.setDestination("/wsApp/multiple.start/111");
        stompHeaders.add("test","111");
        stompHeaders.add("test2","2222");
        stompSession.subscribe(stompHeaders, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {

                System.out.println(index + "--11111------multiple" + new String((byte[])payload));
            }
        });
    }

    public class ReentrantSpinLock {

        private static AtomicReference<Object> sign = new AtomicReference<>();

        public static <T> void lock(T t, boolean reentrantFlag) {
            // 若可重入标志为true, 且若尝试加锁的对象和已加的锁中的对象相同，可重入,并加锁成功
            if (reentrantFlag && t == sign.get()) {
                return;
            }
            //If the lock is not acquired, it can be spun through CAS
            while (!sign.compareAndSet(null, t)) {
                // DO nothing
                System.out.println("自旋一会.");
            }
        }

        public static <T> void unlock(T t) {
            // 锁的线程和目前的线程相等时，才允许释放锁
            if (t == sign.get()) {
                sign.compareAndSet(t, null);
            }
        }
    }
}
