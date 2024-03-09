package com.mnus.common.resp;

import com.mnus.common.exception.ResponseCode;
import com.mnus.common.constance.MDCKey;
import com.mnus.common.exception.ErrorCode;
import org.slf4j.MDC;

import java.io.Serializable;

/**
 * 统一响应
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 9:36:37
 */
public class CommonResp<T> implements Serializable {

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 返回对象
     */
    private T data;
    /**
     * 链路追踪 ID
     */
    private String traceId;

    /**
     * 判断是否响应成功
     *
     * @return ture 成功，false 失败
     */
    public boolean succeed() {
        return ResponseCode.SUCCESS.equals(this.code);
    }

    public static <T> CommonResp<T> success() {
        return success(null, null);
    }

    public static <T> CommonResp<T> success(T data) {
        return success(null, data);
    }

    public static <T> CommonResp<T> successWithMsg(String msg) {
        return success(msg, null);
    }

    public static <T> CommonResp<T> success(String msg, T data) {
        CommonResp<T> resp = new CommonResp<>();
        resp.setCode(ResponseCode.SUCCESS);
        resp.setMsg(msg);
        resp.setData(data);
        return resp;
    }

    public static <T> CommonResp<T> failed(ErrorCode errorCode) {
        return failed(errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> CommonResp<T> failed(Integer code, String msg) {
        return failed(code, msg, null);
    }

    public static <T> CommonResp<T> failed(Integer code, String msg, T data) {
        CommonResp<T> resp = new CommonResp<>();
        resp.setCode(code);
        resp.setMsg(msg);
        resp.setData(data);
        return resp;
    }


    public CommonResp(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public CommonResp(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CommonResp() {
        this.traceId = MDC.get(MDCKey.TID);
    }


    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
