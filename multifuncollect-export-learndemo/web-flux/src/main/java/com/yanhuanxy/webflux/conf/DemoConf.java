package com.yanhuanxy.webflux.conf;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Configurable
public class DemoConf {


    @Bean
    public RouterFunction<ServerResponse> routerFunction(){

        return RouterFunctions.route().GET("/getDetail", RequestPredicates.accept(MediaType.APPLICATION_JSON),
                (ServerRequest request)-> ServerResponse.ok().body(Mono.just("Router-> " + request.queryParam("name").orElse("11")),
                        String.class)).build();
    }


    @Bean
    public ApplicationRunner runner(ApplicationContext context){
        return args -> {
            SimpleUrlHandlerMapping simpleUrlHandlerMapping = context.getBean("simpleUrlHandlerMapping", SimpleUrlHandlerMapping.class);
            HashMap<String, WebHandler> urlMap = new HashMap<>();
            urlMap.put("/get/user/info", (ServerWebExchange exchange) ->
                    exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory()
                            .wrap(("simple name" + exchange.getRequest().getQueryParams().get("name")).getBytes()))));
            simpleUrlHandlerMapping.setUrlMap(urlMap);
            simpleUrlHandlerMapping.initApplicationContext();
        };
    }

    @Bean
    public SimpleUrlHandlerMapping simpleUrlHandlerMapping(){

        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        HashMap<String, WebHandler> urlMap = new HashMap<>();
        urlMap.put("/get/user/info", (ServerWebExchange exchange) ->
                exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory()
                        .wrap(("simple name" + exchange.getRequest().getQueryParams().get("name")).getBytes()))));
        simpleUrlHandlerMapping.setUrlMap(urlMap);
        simpleUrlHandlerMapping.setOrder(0);

        return simpleUrlHandlerMapping;
    }
}
