package com.yanhuanxy.multifunservice.socket.test;

import com.google.gson.Gson;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;

public class CustomStompSessionHandler extends StompSessionHandlerAdapter {

    private final Integer index;

    public CustomStompSessionHandler(Integer index) {
        this.index = index;
    }

    @Override
    public void afterConnected(final StompSession session, StompHeaders connectedHeaders) {
        System.out.println("StompHeaders: " + connectedHeaders.toString());

        //订阅地址，发送端前面没有/user
        String destination = "/wsUser/"+ index + "/goTo/multiple.start";
        //订阅消息
        session.subscribe(destination, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                //todo 只能接收到byte[]数组，没时间研究原因
                System.out.println(index + "--------11multiple" + new String((byte[]) payload));
            }
        });

        //订阅地址，发送端前面没有/user
//        String destination1 = "/wsTopic/wsUser/"+ this.index +"/multiple.start";
//        //订阅消息
//        session.subscribe(destination1, new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return byte[].class;
//            }
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                //todo 只能接收到byte[]数组，没时间研究原因
//                System.out.println(index + "--------multiple" + new String((byte[])payload));
//                Gson gson = new Gson();
//                Map<String, String> map = (Map<String, String>) gson.fromJson(new String((byte[])payload), Map.class);
//                String destination;
//                if(Objects.equals(map.get("code"), "1")){
//                     destination = "/wsApp/multiple.start";
//                }else if(Objects.equals(map.get("code"), "2")){
//                     destination = "/wsApp/multiple.reenter";
//                }else{
//                    return;
//                }
//
//                session.subscribe(destination,new StompFrameHandler() {
//                    @Override
//                    public Type getPayloadType(StompHeaders headers) {
//                        return byte[].class;
//                    }
//
//                    @Override
//                    public void handleFrame(StompHeaders headers, Object payload) {
//                        Gson gson = new Gson();
//                        Map<String, String> map = (Map<String, String>) gson.fromJson(new String((byte[])payload), Map.class);
//                        System.out.println("用户" + index +" ---- code "+ map.get("code") + "mesage "+ map.get("message"));
//                    }
//                });
//            }
//        });
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
                                Throwable exception) {
        System.out.println(session.getSessionId()+ "  " );
        System.out.println(new String(payload) + "--" + new Gson().toJson(headers));
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        exception.printStackTrace();
        System.out.println("transport error.");
    }
}
