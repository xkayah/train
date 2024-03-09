package com.mnus.common.exception;

/**
 * 业务返回状态码
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 10:48:30
 */
public class ResponseCode {
    /**
     * 成功
     */
    public static Integer SUCCESS = 200;
    /**
     * 无权限
     */
    public static Integer UNAUTHORIZED = 401;
    /**
     * 错误
     */
    public static Integer ERROR = 10000;
    /**
     * 实体不存在
     */
    public static Integer ENTITY_NOT_EXIST = 10001;
    /**
     * 请求错误
     */
    public static Integer BAD_REQUEST = 10002;
    /**
     * 服务错误
     */
    public static Integer SERVICE_ERROR = 10003;
    /**
     * docker 错误
     */
    public static Integer DOCKER_ERROR = 10004;
    /**
     * 第三方api 错误
     */
    public static Integer REMOTE_ERROR = 10005;
}
