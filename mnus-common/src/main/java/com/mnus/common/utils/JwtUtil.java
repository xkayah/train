package com.mnus.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.crypto.GlobalBouncyCastleProvider;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * JWT工具类
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/10 14:45:00
 */
@Component
public class JwtUtil {
    private static final Logger LOG = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${mnus.jwt.secret}")
    private String salt;

    public String genToken(Object bean) {
        GlobalBouncyCastleProvider.setUseBouncyCastle(false);
        Map<String, Object> payload = BeanUtil.beanToMap(bean);
        DateTime now = DateTime.now();
        DateTime expireTime = now.offsetNew(DateField.HOUR, 24);
        payload.put(JWTPayload.ISSUED_AT, now);
        payload.put(JWTPayload.EXPIRES_AT, expireTime);
        payload.put(JWTPayload.NOT_BEFORE, now);
        String token = JWTUtil.createToken(payload, salt.getBytes());
        LOG.info("Generate JWT:{}", token);
        return token;
    }

    public boolean validate(String token) {
        GlobalBouncyCastleProvider.setUseBouncyCastle(false);
        JWT jwt = null;
        try {
            jwt = JWTUtil.parseToken(token).setKey(salt.getBytes());
        } catch (Exception e) {
            throw new JSONException("token format error!");
        }
        boolean validate = jwt.validate(0);
        LOG.info("JWT：{}... ,validated：{}", token.substring(0, 10), validate);
        return validate;
    }

    public JSONObject parseJson(String token) {
        JWT jwt = JWTUtil.parseToken(token).setKey(salt.getBytes());
        JSONObject payloads = jwt.getPayloads();
        payloads.remove(JWTPayload.ISSUED_AT);
        payloads.remove(JWTPayload.EXPIRES_AT);
        payloads.remove(JWTPayload.NOT_BEFORE);
        LOG.info("Original JWT：{}", payloads);
        return payloads;
    }
}
