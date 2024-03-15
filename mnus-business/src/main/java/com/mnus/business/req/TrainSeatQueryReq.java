package com.mnus.business.req;

import com.mnus.common.req.PageReq;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
public class TrainSeatQueryReq extends PageReq {

    private String trainCode;

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    @Override
    public String toString() {
        return "TrainSeatQueryReq{" +
                "trainCode='" + trainCode + '\'' +
                "} " + super.toString();
    }
}