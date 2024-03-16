package com.mnus.batch.controller;

import com.mnus.batch.feign.TestFeign;
import jakarta.annotation.Resource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/8 12:12:56
 */
@RestController
public class TestController {
    @Resource
    private Environment env;

    @Resource
    private TestFeign testFeign;

    @GetMapping("/hello")
    public String hello() {
        System.out.printf("[feign]:%s\n", testFeign.hello());
        return String.format("Hello %s Service!", env.getProperty("spring.application.name"));
    }
}
