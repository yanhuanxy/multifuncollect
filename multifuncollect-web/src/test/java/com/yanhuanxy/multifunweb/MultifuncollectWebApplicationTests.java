package com.yanhuanxy.multifunweb;

import com.yanhuanxy.multifunservice.socket.test.MultipleCompetitionWebsocketClient;
import org.java_websocket.client.WebSocketClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 *          <dependency>
 *               <groupId>org.springframework.boot</groupId>
 * *             <artifactId>spring-boot-starter-test</artifactId>
 * *         </dependency>
 *
 *
 * 如果项目中引入了websocket
 * @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
 *
 * 原因 用@SpringBootTest(classes= {Pcm*******Application.class})
 * 会报  Caused by: java.lang.IllegalStateException: javax.websocket.server.ServerContainer not availabl 错
 */
//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest(classes = MultifuncollectWebApplication.class)
public class MultifuncollectWebApplicationTests {

    @Test
    public void contextLoads() {
    }


    public static void main(String[] args) {
        MultipleCompetitionWebsocketClient multipleCompetitionWebsocketClient = new MultipleCompetitionWebsocketClient();
        WebSocketClient connect = multipleCompetitionWebsocketClient.connect("ws://192.168.0.98:8888/knowledge/multipleMatch/1/1", null);
//        multipleCompetitionWebsocketClient.connect("ws://192.168.0.98:8889/multifuncollect/multipleMatch/1/1", null);
        connect.send("111");
    }


}
