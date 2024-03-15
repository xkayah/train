package com.mnus.business.req;

import com.mnus.common.req.PageReq;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
public class TrainQueryReq extends PageReq {

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "TrainQueryReq{" +
                "code='" + code + '\'' +
                "} " + super.toString();
    }
}