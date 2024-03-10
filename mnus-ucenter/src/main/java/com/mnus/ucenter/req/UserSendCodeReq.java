package com.mnus.ucenter.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 9:20:31
 */
public class UserSendCodeReq {
    @NotEmpty(message = "[手机号]不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "[手机号]格式不正确")
    private String mobile;

    public UserSendCodeReq(String mobile) {
        this.mobile = mobile;
    }

    public UserSendCodeReq() {
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserSendCodeReq{");
        sb.append("mobile='").append(mobile).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
