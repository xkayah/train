package com.mnus.business.config;

import com.mnus.common.interceptor.CollectorInterceptor;
import com.mnus.common.interceptor.TidInterceptor;
import com.mnus.common.interceptor.TokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 拦截器配置类
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/11 22:38:52
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Resource
    private CollectorInterceptor collectorInterceptor;
    @Resource
    private TidInterceptor tidInterceptor;
    @Resource
    private TokenInterceptor tokenInterceptor;

    private final List<String> PUBLIC_PATH = List.of(
            "/hello");

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tidInterceptor);
        registry.addInterceptor(tokenInterceptor).
                excludePathPatterns(PUBLIC_PATH);
        registry.addInterceptor(collectorInterceptor).
                excludePathPatterns(PUBLIC_PATH);
    }
}
