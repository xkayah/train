package com.mnus.common.exception;

/**
 * 业务返回状态码
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 10:48:30
 */
public class ResponseCode {
    public static Integer SUCCESS = 200;
    public static Integer UNAUTHORIZED = 401;
    public static Integer ERROR = 10000;
    public static Integer ENTITY_NOT_EXIST = 10001;
    public static Integer BAD_REQUEST = 10002;
    public static Integer SERVICE_ERROR = 10003;
    public static Integer DOCKER_ERROR = 10004;
}
