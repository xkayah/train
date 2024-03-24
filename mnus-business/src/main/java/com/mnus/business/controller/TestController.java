package com.mnus.business.controller;

import com.mnus.business.mapper.TrainMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/8 12:12:56
 */
@RestController
public class TestController {
    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);
    @Resource
    private Environment env;
    @Resource
    private TrainMapper mapper;

    @GetMapping("/hello")
    public String hello() {
        return String.format("Hello %s Service!", env.getProperty("spring.application.name"));
    }

    @GetMapping("/test")
    @Transactional
    public String test() {
        LOG.info("开始查询...");
        mapper.selectByExample(null);
        mapper.selectByExample(null);
        mapper.selectByExample(null);
        return String.format("Hello %s Service!", env.getProperty("spring.application.name"));
    }


}
