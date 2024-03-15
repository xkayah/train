package com.mnus.business.req;

import com.mnus.common.req.PageReq;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
public class StationQueryReq extends PageReq {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StationQueryReq{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}