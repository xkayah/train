package com.mnus.train12306.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/8 11:29:06
 */
@SpringBootApplication
@ComponentScan("com.mnus")
public class UCenterApplication {
    private static final Logger LOG = LoggerFactory.getLogger(UCenterApplication.class);
    public static void main(String[] args) {

        Environment env = SpringApplication.run(UCenterApplication.class).getEnvironment();
        LOG.info("ucenter start! test url:\thttp://127.0.0.1:{}{}/hello",
                env.getProperty("server.port"),
                env.getProperty("server.servlet.context-path"));
    }
}