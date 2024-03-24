package com.mnus.business.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/24 10:18:47
 */
@Configuration
public class FeignConfig {

    /**
     * feign 在远程调用之前会执行所有的 RequestInterceptor 拦截器
     *
     * @return
     */
    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                // 1、使用 RequestContextHolder 拿到请求数据,RequestContextHolder 底层使用过线程共享数据 ThreadLocal<RequestAttributes>
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    // 2、同步请求头数据
                    String authorization = request.getHeader("Authorization");
                    Object uidAttr = request.getAttribute("uid");
                    if (Objects.nonNull(uidAttr)) {
                        String uid = uidAttr.toString();
                        request.setAttribute("uid", uid);
                    }
                    // 给新请求同步了旧请求的数据
                    requestTemplate.header("Authorization", authorization);
                }
            }
        };
    }
}
