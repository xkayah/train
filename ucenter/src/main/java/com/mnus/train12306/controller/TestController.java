package com.mnus.train12306.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/8 12:12:56
 */
@RestController
public class TestController {
    @GetMapping("/hello")
    public String hello(){
        return "Hello UCenter Server!";
    }
}
