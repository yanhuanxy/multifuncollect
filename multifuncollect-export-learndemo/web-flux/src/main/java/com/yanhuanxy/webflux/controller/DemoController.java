package com.yanhuanxy.webflux.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/getInfo")
    public String getMessageInfo(){

        return "test flux web";
    }
}
