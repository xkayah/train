package com.mnus.ucenter.req;

import com.mnus.common.req.PageReq;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/11 12:15:08
 */
public class PassengerQueryReq extends PageReq {
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PassengerQueryReq{");
        sb.append("userId=").append(userId);
        sb.append('}');
        return sb.toString();
    }
}
