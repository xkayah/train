package com.mnus.business.req;

import com.mnus.common.req.PageReq;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
public class DailyTrainTicketQueryReq extends PageReq {

    /**
     * 日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    /**
     * 车次编号
     */
    private String trainCode;

    /**
     * 出发站
     */
    private String start;

    /**
     * 到达站
     */
    private String end;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DailyTrainTicketQueryReq that = (DailyTrainTicketQueryReq) o;

        if (!date.equals(that.date)) return false;
        if (!trainCode.equals(that.trainCode)) return false;
        if (!start.equals(that.start)) return false;
        if (!getPageNo().equals(that.getPageNo())) return false;
        if (!getPageSize().equals(that.getPageSize())) return false;
        return end.equals(that.end);
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + trainCode.hashCode();
        result = 31 * result + start.hashCode();
        result = 31 * result + end.hashCode();
        result = 31 * result + getPageNo().hashCode();
        result = 31 * result + getPageSize().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DailyTrainTicketQueryReq{" +
                "date=" + date +
                ", trainCode='" + trainCode + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                "} " + super.toString();
    }
}