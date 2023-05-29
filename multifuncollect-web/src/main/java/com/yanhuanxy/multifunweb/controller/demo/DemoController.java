package com.yanhuanxy.multifunweb.controller.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author hxl
 */
@RequestMapping("/demo")
@RestController
public class DemoController {

    @GetMapping("/uuid")
    public String captchaByUUID() throws IOException {
        return UUID.randomUUID().toString();
    }

}
