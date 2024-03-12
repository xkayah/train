package com.mnus.common.interceptor;

import com.mnus.common.constance.MDCKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * tid 拦截器,优先级最高
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/11 20:41:47
 */
@Component
public class TidInterceptor implements HandlerInterceptor, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(TidInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put(MDCKey.TID, UUID.randomUUID().toString());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(MDCKey.TID);
    }

    @Override
    public int getOrder() {
        return -3;
    }
}
