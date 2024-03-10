package com.mnus.ucenter.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 9:20:31
 */
public class LoginOrRegistryReq {
    @NotEmpty(message = "[手机号]不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$")
    private String mobile;

    @NotEmpty(message = "[验证码]不能为空")
    private String code;

    public LoginOrRegistryReq(String mobile, String code) {
        this.mobile = mobile;
        this.code = code;
    }

    public LoginOrRegistryReq() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserRegistryReq{");
        sb.append("mobile='").append(mobile).append('\'');
        sb.append(", code='").append(code).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
