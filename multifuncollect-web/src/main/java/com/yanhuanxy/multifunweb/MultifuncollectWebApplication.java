package com.yanhuanxy.multifunweb;

import org.apache.catalina.connector.Connector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@ComponentScan({"com.yanhuanxy"})
@MapperScan("com.yanhuanxy.multifundao.mybatis.**")
@EnableRedisRepositories("com.yanhuanxy.multifundao.redis.**")
@SpringBootApplication
public class MultifuncollectWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultifuncollectWebApplication.class, args);
    }

    @Value("${server.http-port}")
    private Integer httpPort;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(httpPort);
        tomcat.addAdditionalTomcatConnectors(connector);
        return tomcat;
    }
}
