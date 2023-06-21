package com.yanhuanxy.multifunservice.socket.test;

import com.google.gson.Gson;
import com.yanhuanxy.multifuncommon.exception.BaseRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.util.Map;


public class MultipleCompetitionWebsocketClient {
    private Logger log = LoggerFactory.getLogger(MultipleCompetitionWebsocketClient.class);

    private String cerPath = "classpath:static/multifuncollectclient.pkcs12";

    private String cerPwd = "kelven";

    /**
     * wss协议证书认证
     *
     * @param webSocketClient
     */
    public void createWebSocketClient(WebSocketClient webSocketClient) {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream inputStream = resourceLoader(cerPath);
            keyStore.load(inputStream, cerPwd.toCharArray());
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(keyStore, new TrustSelfSignedStrategy()).build();
            SSLSocketFactory sslfactory = sslContext.getSocketFactory();
            webSocketClient.setSocketFactory(sslfactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加故障消息类型
     *
     * @param message
     * @param type
     * @return
     */
    public String addTypeOfMsg(String message, String type) {
        try {
            Map<String,String> json = new Gson().fromJson(message, Map.class);
            json.put("msgType", type);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取文件信息
     *
     * @param fileFullPath
     * @return
     * @throws IOException
     */
    public InputStream resourceLoader(String fileFullPath) throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        return resourceLoader.getResource(fileFullPath).getInputStream();
    }

    // 0：链接断开或者异常；1：代表链接中；2：代表正在连接；
    public static int isConnect = 0;

    // 消息类型
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private WebSocketClient wsClient;

    public WebSocketClient getWsClient() {
        return wsClient;
    }

    public void setWsClient(WebSocketClient wsClient) {
        this.wsClient = wsClient;
    }

    public WebSocketClient createWebSocketClient(String wsUri, Map<String, String> httpHeaders) {

        try {
            //创建客户端连接对象
            WebSocketClient client = new WebSocketClient(new URI(wsUri), httpHeaders) {

                /**
                 * 建立连接调用
                 * @param serverHandshake
                 */
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    log.info("WsClientOfLocal -> onOpen -> 客户端建立连接");
                    isConnect = 1;
                }

                /**
                 * 收到服务端消息调用
                 * @param s
                 */
                @Override
                public void onMessage(String s) {
                    log.info("WsClientOfLocal -> onMessage -> 收到服务端消息：{}", s);
                    // 如果对接多个WebSocket服务，且服务消息处理方案一样，
                    // 则可以在接收到消息的第一时间为该消息添加区分类型，
                    // 比如IP地址：200，201，202
                    s = addTypeOfMsg(s, type);
                    if (StringUtils.isNotBlank(s)) {
                        // 统一处理消息
//                        messageService.handleMessage(s);
                    }
                }

                /**
                 * 断开连接调用
                 * @param i
                 * @param s
                 * @param b
                 */
                @Override
                public void onClose(int i, String s, boolean b) {
                    log.info("WsClientOfLocal -> onClose -> 客户端关闭连接，i：{}，b：{}，s：{}", i, b, s);
                    isConnect = 0;
                }

                /**
                 * 连接报错调用
                 * @param e
                 */
                @Override
                public void onError(Exception e) {
                    log.error("WsClientOfLocal -> onError -> 客户端连接异常，异常信息：{}", e.getMessage());
                    if (null != wsClient) {
                        wsClient.close();
                    }
                    isConnect = 0;
                }
            };
            return client;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 连接websocket服务端
     * 注意 synchronized 关键字，保证多个请求同时连接时，
     * 只有一个连接在创建
     *
     * @param uri
     * @param httpHeaders
     * @return
     */
    public synchronized WebSocketClient connect(String uri, Map<String, String> httpHeaders) {
        WebSocketClient oldWsClient = this.getWsClient();
        if (null != oldWsClient) {
            log.info("WsClientOfLocal -> 已存在连接，oldWsClient：{}-{}",
                    oldWsClient.getReadyState(), oldWsClient.getReadyState().ordinal());
            if (1 == oldWsClient.getReadyState().ordinal()) {
                log.info("WsClientOfLocal -> 使用存在且已打开的连接");
                return oldWsClient;
            } else {
                log.info("WsClientOfLocal -> 注销存在且未打开的连接，并重新获取新的连接");
                oldWsClient.close();
            }
        }

        WebSocketClient newWsClient = createWebSocketClient(uri, httpHeaders);
        // 如果是 "wss" 协议，则进行证书认证，认证方法在父类中
        if (uri.startsWith("wss")) {
            createWebSocketClient(newWsClient);
        }
        if (null == newWsClient) {
            // 自定义异常
            throw new BaseRuntimeException( "WsClientOfLocal -> 创建失败！");
        }
        newWsClient.connect();
        // 设置连接状态为正在连接
        isConnect = 2;
        // 连接状态不再是0请求中，判断建立结果是不是1已建立
        long startTime = System.currentTimeMillis();
        while (1 != newWsClient.getReadyState().ordinal()) {
            // 避免网络波动，设置持续等待连接时间
            long endTime = System.currentTimeMillis();
            long waitTime = (endTime - startTime) / 1000;
            if (5L < waitTime) {
                log.info("WsClientOfLocal -> 建立连接异常，请稍后再试");
                break;
            }
        }
        if (1 == newWsClient.getReadyState().ordinal()) {
            this.setWsClient(newWsClient);
            newWsClient.send("WsClientOfLocal -> 客户端连接成功！");
            log.info("WsClientOfLocal -> 客户端连接成功！");
            return newWsClient;
        }
        return null;
    }
}
