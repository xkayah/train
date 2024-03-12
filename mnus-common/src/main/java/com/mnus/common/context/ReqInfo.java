package com.mnus.common.context;

/**
 * web请求信息类
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/11 22:07:58
 */
public class ReqInfo {
    private Long uid;
    private String ip;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ReqInfo{");
        sb.append("uid=").append(uid);
        sb.append(", ip='").append(ip).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
