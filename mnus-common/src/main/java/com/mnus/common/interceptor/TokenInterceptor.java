package com.mnus.common.interceptor;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mnus.common.context.UserLoginResp;
import com.mnus.common.enums.BaseErrorCodeEnum;
import com.mnus.common.exception.BizException;
import com.mnus.common.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.Optional;

/**
 * token 拦截器
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/11 18:55:15
 */
@Component
public class TokenInterceptor implements HandlerInterceptor, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(TokenInterceptor.class);
    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_SCHEMA = "Bearer ";
    public static final String REQUEST_ATTRIBUTE_UID = "uid";
    @Resource
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authCL = getToken(request);
        JSONObject jsonObject = null;
        try {
            jsonObject = jwtUtil.parseJson(authCL);
        } catch (Exception e) {
            throw new BizException(BaseErrorCodeEnum.UNAUTHORIZED);
        }
        UserLoginResp userLoginResp = JSONUtil.toBean(jsonObject, UserLoginResp.class);
        String id = userLoginResp.getId();
        if (Objects.nonNull(id)) {
            request.setAttribute(REQUEST_ATTRIBUTE_UID, id);
        }
        return true;
    }

    private String getToken(HttpServletRequest request) {
        String authCL = request.getHeader(AUTH_HEADER);
        return Optional.ofNullable(authCL)
                .filter(a -> a.startsWith(AUTH_SCHEMA))
                .map(a -> a.substring(AUTH_SCHEMA.length()))
                .orElse(null);
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
