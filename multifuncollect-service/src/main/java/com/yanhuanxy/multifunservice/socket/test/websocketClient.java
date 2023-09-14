package com.yanhuanxy.multifunservice.socket.test;

import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class websocketClient {
    private static volatile Object object = new Object();

    public static void main(String[] args) {
//        Map<Integer, String> head = new HashMap<>();
//        head.put(8, "eyJhbGciOiJIUzUxMiJ9.eyJMT0dJTl9VU0VSX0tFWV8iOiI1NzY3ZGQ4Mi1hN2EwLTQ4ZDYtODExYi05MTg0ZTJkOWMzYzYiLCJjbGFpbXNfY291bnRfa2V5IjoidGVzdDYiLCJpc3MiOiJrbm93bGVkZ2UiLCJzdWIiOiJVU0VSTkFNRSIsImF1ZCI6IldlYiIsImlhdCI6MTY4NjU1NzMzMn0.Kch4AR215VaKOGbAkdHJVuUzbZJzUAbqJOGzPaiuGMIvuuLio0qZfGCTBZH2gy6qNex6lezhdQ4vGsSfvEUPQA");
//        head.put(9, "eyJhbGciOiJIUzUxMiJ9.eyJMT0dJTl9VU0VSX0tFWV8iOiJkNDQzNzliNy1mZjE5LTQ3NTYtYmNjNy1lMjNjZDczMzgwMzMiLCJjbGFpbXNfY291bnRfa2V5IjoidGVzdDciLCJpc3MiOiJrbm93bGVkZ2UiLCJzdWIiOiJVU0VSTkFNRSIsImF1ZCI6IldlYiIsImlhdCI6MTY4NjU1NzM2MX0.uIw_6DUkE-8-C82H9I1KOl6JEgE1JELjkm0BDn5dUWGb4wdGzVfHzzZg5CakY9TxZCTjWqcTKuRyvchvh83jqA");
//        creatThread(2, 8, head);
//
//        Map<Integer, String> head2 = new HashMap<>();
//        head2.put(10, "eyJhbGciOiJIUzUxMiJ9.eyJMT0dJTl9VU0VSX0tFWV8iOiI3OTY0MWI2Ni05YjE3LTQyNjQtODhmZi1mNDM3MWYzNTNjZWMiLCJjbGFpbXNfY291bnRfa2V5IjoidGVzdDgiLCJpc3MiOiJrbm93bGVkZ2UiLCJzdWIiOiJVU0VSTkFNRSIsImF1ZCI6IldlYiIsImlhdCI6MTY4NjU1NzQwM30.N5YT-WVPZHBEQLURXcpKhD7HKPnSSPtH5TZHrQm7KRkwM0uEJqvfhVOvl6NF9qzeEI3S7lpXpZMrpXNFScHsVg");
//        head2.put(11, "eyJhbGciOiJIUzUxMiJ9.eyJMT0dJTl9VU0VSX0tFWV8iOiIyM2I1ZTEzYS0xZTUzLTRkYmYtODdkZi05OGZlY2M3ZGFjOGMiLCJjbGFpbXNfY291bnRfa2V5IjoidGVzdDkiLCJpc3MiOiJrbm93bGVkZ2UiLCJzdWIiOiJVU0VSTkFNRSIsImF1ZCI6IldlYiIsImlhdCI6MTY4NjU1NzQzNX0.vHQvfZAiLW1z5AG56RZSbrhNIOzJxzjxag6NBxcQ4H_JEhOe1H2A9uoCkHQQX9lKu6fpKGnul-irhLr0tvZVWg");
//        creatThread(2, 10,head2);

        System.out.println(0b0111);
        System.out.println(0b0101);
        long aaa = 1_23467_2455222222L;
        System.out.println(aaa);
    }



    public static void creatThread(int num, int start, Map<Integer, String> head){
        for (int i = start; i < start + num; i++) {
            int finalI = i;

            new Thread(()->{
                try {
                    websocketClient stompClient = new websocketClient();
                    final CountDownLatch latch = new CountDownLatch(1);
                    // 建立连接 进行匹配
                    WebSocketClient webSocketClient = stompClient.websocketClient(finalI, head.get(finalI));
                    // 等待30秒 假装掉线 重新进入 2 -> 重新加入
                    // 成功返回房间信息
                    latch.await(10, TimeUnit.SECONDS);
                    Gson gson = new Gson();
                    Map<String, Object> map = new HashMap<>();
                    map.put("messageType", 2);
                    map.put("content", "测试");
                    map.put("sender", finalI+"");
                    webSocketClient.send(gson.toJson(map));
                    // 再次等待三十秒 上一个匹配还在进行中申请重新匹配  3-> 重新匹配
                    // 失败返回上一个游戏还在进行中
                    latch.await(10, TimeUnit.SECONDS);
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("messageType", 3);
                    map2.put("content", "我想重新开始");
                    map2.put("sender", finalI+"");
                    webSocketClient.send(gson.toJson(map2));
                    // 等待 3分钟 上一个比赛已结束 房间未关闭
                    // 等待五分钟  上一个比赛已结束 房间已关闭
                    // 正确 正常匹配完成 返回题目信息
                    latch.await(60, TimeUnit.SECONDS);
                    Map<String, Object> map3 = new HashMap<>();
                    map3.put("messageType", 2);
                    map3.put("content", "测试");
                    map3.put("sender", finalI+"");
                    webSocketClient.send(gson.toJson(map3));
                    // 等待十秒 假装掉线 重新进入
                    latch.await(10, TimeUnit.SECONDS);
                    Map<String, Object> map4 = new HashMap<>();
                    map4.put("messageType", 3);
                    map4.put("content", "我想重新开始");
                    map4.put("sender", finalI+"");
                    webSocketClient.send(gson.toJson(map4));
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//                    webSocketClient.closeConnection(1, "xxx");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * 开启连接
     */
    private WebSocketClient websocketClient(int index, String authorization) throws Exception{
        Draft_6455 draft_6455 = new Draft_6455();
        WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://192.168.0.98:8888/knowledge/multipleCompetition/"+ index), draft_6455) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                System.out.println("用户："+ index+ " websocket客户端和服务器连接成功");
            }

            @Override
            public void onMessage(String message) {
                System.out.println("用户："+ index+ "websocket客户端收到消息={}"+  message);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                System.out.println("用户："+ index+ "websocket客户端退出连接");
            }

            @Override
            public void onError(Exception e) {
                System.out.println("websocket客户端和服务器连接发生错误={}" + e.getMessage());
                e.printStackTrace();
            }
        };

        webSocketClient.addHeader("Sec-WebSocket-Protocol", authorization);
        webSocketClient.connect();
        return webSocketClient;
    }

}
