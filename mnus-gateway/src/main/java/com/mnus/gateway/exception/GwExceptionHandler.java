package com.mnus.gateway.exception;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 网关异常处理
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/11 10:37:07
 */
@RestControllerAdvice
public class GwExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GwExceptionHandler.class);

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(JSONException.class)
    public String runtimeExceptionHandler(Throwable e) {
        LOG.error("引发的异常的堆栈信息：", e);
        return e.getMessage();
    }

}
