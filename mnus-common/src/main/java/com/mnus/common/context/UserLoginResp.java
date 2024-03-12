package com.mnus.common.context;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 9:20:31
 */
public class UserLoginResp {
    private String uname;
    private String id;
    private String token;

    public UserLoginResp() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserLoginResp{");
        sb.append("uname='").append(uname).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", token='").append(token).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
