package com.mnus.business.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;


/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
public class ConfirmOrderSubmitReq {
    /**
     * 会员id
     */
    private Long userId;
    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull(message = "[日期]不能为空")
    private Date date;
    /**
     * 车次编号
     */
    @NotBlank(message = "[车次编号]不能为空")
    private String trainCode;
    /**
     * 出发站
     */
    @NotBlank(message = "[出发站]不能为空")
    private String start;
    /**
     * 到达站
     */
    @NotBlank(message = "[到达站]不能为空")
    private String end;
    /**
     * 余票ID
     */
    @NotNull(message = "[余票ID]不能为空")
    private Long dailyTrainTicketId;
    /**
     * 车票
     * {
     * passengerId: 123,
     * passengerType: "1",
     * passengerName: "Zhang San",
     * passengerIdCard: "123123123",
     * seatTypeCode: "1",
     * seat: "C1",
     * }
     */
    @NotEmpty(message = "[车票]不能为空")
    private List<ConfirmOrderTicketReq> tickets;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public Long getDailyTrainTicketId() {
        return dailyTrainTicketId;
    }

    public void setDailyTrainTicketId(Long dailyTrainTicketId) {
        this.dailyTrainTicketId = dailyTrainTicketId;
    }

    public void setTickets(List<ConfirmOrderTicketReq> tickets) {
        this.tickets = tickets;
    }

    public List<ConfirmOrderTicketReq> getTickets() {
        return tickets;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", date=").append(date);
        sb.append(", trainCode=").append(trainCode);
        sb.append(", start=").append(start);
        sb.append(", end=").append(end);
        sb.append(", dailyTrainTicketId=").append(dailyTrainTicketId);
        sb.append(", tickets=").append(tickets);
        sb.append("]");
        return sb.toString();
    }
}
