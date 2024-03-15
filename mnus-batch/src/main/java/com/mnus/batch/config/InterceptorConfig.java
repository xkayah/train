package com.mnus.batch.config;

import com.mnus.common.interceptor.TidInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置类
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/11 22:38:52
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Resource
    private TidInterceptor tidInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tidInterceptor);
    }
}
