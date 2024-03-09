package com.mnus.common.exception;

/**
 * 自定义业务异常
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 9:57:02
 */
public class BizException extends AbstractException {
    public BizException(String msg) {
        super(msg);
    }

    public BizException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BizException(ErrorCode errorCode, String info, Throwable cause) {
        super(errorCode, info, cause);
    }

    public BizException(ErrorCode errorCode) {
        super(errorCode);
    }
}
