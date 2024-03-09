package com.mnus.ucenter.resp;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 9:20:31
 */
public class UserLoginResp {
    private String uname;

    private String id;

    public UserLoginResp(String uname, String code) {
        this.uname = uname;
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
        sb.append('}');
        return sb.toString();
    }
}
