package com.mnus.common.exception;

/**
 * 异常码
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 10:16:25
 */
public interface ErrorCode {
    /**
     * error code
     *
     * @return code
     */
    Integer getCode();

    /**
     * error info
     *
     * @return msg
     */
    String getMsg();
}
