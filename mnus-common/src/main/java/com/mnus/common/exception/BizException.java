package com.mnus.common.exception;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 9:57:02
 */
public class BizException extends RuntimeException {

    private String errMsg;

    public BizException(String errMsg) {
        super(errMsg);
    }
}
