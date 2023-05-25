package com.yanhuanxy.multifunweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.yanhuanxy"})
@MapperScan("com.yanhuanxy.multifundao.**")
@SpringBootApplication
public class MultifuncollectWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultifuncollectWebApplication.class, args);
    }

}
