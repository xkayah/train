package com.mnus.ucenter.req;

import jakarta.validation.constraints.NotEmpty;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 9:20:31
 */
public class UserRegistryReq {
    private Long id;

    @NotEmpty(message = "[手机号]不能为空")
    private String mobile;

    public UserRegistryReq(Long id, String mobile) {
        this.id = id;
        this.mobile = mobile;
    }

    public UserRegistryReq() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserLoginReq{");
        sb.append("id=").append(id);
        sb.append(", mobile='").append(mobile).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
