package com.mnus.common.interceptor;

import com.mnus.common.context.ReqHolder;
import com.mnus.common.context.ReqInfo;
import com.mnus.common.utils.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

/**
 * 用户信息拦截器
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/11 22:03:59
 */
@Component
public class CollectorInterceptor implements HandlerInterceptor, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(CollectorInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ReqInfo info = new ReqInfo();
        info.setUid(Optional.ofNullable(request.getAttribute(TokenInterceptor.REQUEST_ATTRIBUTE_UID)).map(Object::toString).map(Long::parseLong).orElse(null));
        info.setIp(IpUtil.getClientIp(request));
        ReqHolder.set(info);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ReqHolder.remove();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
