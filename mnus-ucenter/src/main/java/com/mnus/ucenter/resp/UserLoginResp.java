package com.mnus.ucenter.resp;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 9:20:31
 */
public class UserLoginResp {
    private String mobile;

    private String id;

    public UserLoginResp(String mobile, String code) {
        this.mobile = mobile;
        this.id = code;
    }

    public UserLoginResp() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        final StringBuffer sb = new StringBuffer("UserLoginResp{");
        sb.append("mobile='").append(mobile).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
