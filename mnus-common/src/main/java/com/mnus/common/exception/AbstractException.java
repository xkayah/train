package com.mnus.common.exception;

import com.mnus.common.resp.CommonResp;

/**
 * 抽象异常类
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 12:26:37
 */
public abstract class AbstractException extends RuntimeException {
    private CommonResp responseBody;

    public AbstractException(String msg) {
        this(ResponseCode.BAD_REQUEST, msg, null, null);
    }

    public AbstractException(String msg, Throwable cause) {
        this(ResponseCode.BAD_REQUEST, msg, null, cause);
    }

    public AbstractException(ErrorCode errorCode, String info, Throwable cause) {
        this(errorCode.getCode(), errorCode.getMsg(), null, cause);
    }

    public AbstractException(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMsg(), null, null);
    }

    private AbstractException(Integer code, String msg, String info, Throwable cause) {
        super(msg, cause);
        if (info == null) {
            this.responseBody = new CommonResp(code, msg);
        } else {
            this.responseBody = new CommonResp(code, msg + ":" + info);
        }
    }


    // 考虑性能问题，只打印顶层堆栈
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public CommonResp getResponseBody() {
        return responseBody;
    }
}
