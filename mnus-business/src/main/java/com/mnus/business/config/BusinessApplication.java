package com.mnus.business.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/8 11:29:06
 */
@SpringBootApplication
@ComponentScan("com.mnus")
@MapperScan("com.mnus.*.mapper")
@EnableFeignClients("com.mnus.business.feign")
public class BusinessApplication {
    private static final Logger LOG = LoggerFactory.getLogger(BusinessApplication.class);

    public static void main(String[] args) {
        Environment env = SpringApplication.run(BusinessApplication.class).getEnvironment();
        String path = env.getProperty("server.servlet.context-path");
        if (!StringUtils.hasLength(path)) {
            path = "";
        }
        LOG.info("Hello {}! test url:\thttp://127.0.0.1:{}{}/hello",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                path
        );
    }
}