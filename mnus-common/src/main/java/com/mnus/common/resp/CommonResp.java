package com.mnus.common.resp;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 9:36:37
 */
public class CommonResp<T> {
    /**
     * 成功标识
     */
    private Boolean success;
    /**
     * 错误码
     */
    private Integer errCode;
    /**
     * 错误信息
     */
    private String errMsg;
    /**
     * 返回对象
     */
    private T content;

    public static <T> CommonResp<T> ok() {
        CommonResp<T> result = new CommonResp<T>();
        result.setContent(null);
        result.setSuccess(Boolean.TRUE);
        return result;
    }

    public static <T> CommonResp<T> ok(T data) {
        CommonResp<T> result = new CommonResp<T>();
        result.setContent(data);
        result.setSuccess(Boolean.TRUE);
        return result;
    }

    public static <T> CommonResp<T> fail(Integer code, String msg) {
        CommonResp<T> result = new CommonResp<T>();
        result.setSuccess(Boolean.FALSE);
        result.setErrCode(code);
        result.setErrMsg(msg);
        return result;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public CommonResp(Boolean success, Integer errCode, String errMsg, T data) {
        this.success = success;
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.content = data;
    }

    public CommonResp() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
