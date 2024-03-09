package com.mnus.common.exception.handler;

import com.mnus.common.exception.BizException;
import com.mnus.common.resp.CommonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 9:54:00
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Throwable.class)
    public CommonResp<?> runtimeExceptionHandler(Throwable e) {
        LOG.error("引发的异常的堆栈信息：", e);
        return CommonResp.fail(500, "系统错误");
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BizException.class)
    public CommonResp<?> bizExceptionHandler(BizException e) {
        LOG.error("引发的异常的堆栈信息：", e);
        return CommonResp.fail(500, e.getMessage());
    }

}
