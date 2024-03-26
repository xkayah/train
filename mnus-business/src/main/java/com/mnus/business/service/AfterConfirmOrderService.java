package com.mnus.business.service;

import cn.hutool.core.date.DateTime;
import com.mnus.business.domain.ConfirmOrder;
import com.mnus.business.domain.DailyTrainSeat;
import com.mnus.business.domain.DailyTrainTicket;
import com.mnus.business.enums.ConfirmOrderStatusEnum;
import com.mnus.business.feign.UcenterFeign;
import com.mnus.business.mapper.ConfirmOrderMapper;
import com.mnus.business.mapper.DailyTrainSeatMapper;
import com.mnus.business.mapper.my.MyDailyTrainTicketMapper;
import com.mnus.business.req.ConfirmOrderTicketReq;
import com.mnus.common.context.ReqHolder;
import com.mnus.common.req.TicketInsertReq;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
@Service
public class AfterConfirmOrderService {
    private static final Logger LOG = LoggerFactory.getLogger(AfterConfirmOrderService.class);
    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;
    @Resource
    private MyDailyTrainTicketMapper myDailyTrainTicketMapper;
    @Resource
    private UcenterFeign ucenterFeign;
    @Resource
    private ConfirmOrderMapper confirmOrderMapper;

    /**
     * 提交之后的动作
     *
     * @param ticket         售卖的每日车票,注意 [start, end)
     * @param chosenSeatList 选中的座位列表
     * @param tickets        用户提交的车票信息
     * @param confirmOrder
     */
    // @Transactional
    // @GlobalTransactional
    public void afterSubmit(DailyTrainTicket ticket, List<DailyTrainSeat> chosenSeatList, List<ConfirmOrderTicketReq> tickets, ConfirmOrder confirmOrder) {
        // LOG.info("[XID]{}", RootContext.getXID());
        String trainCode = ticket.getTrainCode();
        Date date = ticket.getDate();
        String start = ticket.getStart();
        String end = ticket.getEnd();
        for (int i = 0; i < chosenSeatList.size(); i++) {
            DailyTrainSeat seat = chosenSeatList.get(i);
            // 1.根据选中的座位列表更新座位售卖情况
            DailyTrainSeat record = new DailyTrainSeat();
            record.setId(seat.getId());
            record.setSell(seat.getSell());
            record.setGmtModified(new Date());
            dailyTrainSeatMapper.updateByPrimaryKeySelective(record);

            // 2.更新影响车票的余票,范围扣减:
            // before: 01100000001
            // toSell  :    1111
            // after : 01100111101
            // effect: 011EEEEEEE1
            char[] chs = record.getSell().toCharArray();
            int startIdx = ticket.getStartIndex();
            int endIdx = ticket.getEndIndex();
            int minStartIdx = 0;
            int maxStartIdx = startIdx + 1;
            int maxEnd = chs.length;
            int minEndIdx = endIdx - 1;
            // minStartIdx = 从 startIdx 往前找第一个1.直接从 startIdx 开始,不要-1,否则可能会越界
            for (int idx = startIdx; idx > 0; idx--) {
                if (chs[idx] == '1') {
                    minStartIdx = idx;
                    break;
                }
            }
            // maxEnd = 从 endIdx 往后找第一个1.直接从 endIdx 开始,不要+1,否则可能会越界
            for (int idx = endIdx; idx < chs.length; idx++) {
                if (chs[idx] == '1') {
                    maxEnd = idx;
                    break;
                }
            }
            String seatType = seat.getSeatType();
            long count = myDailyTrainTicketMapper.updateCountBySell(
                    date, trainCode, seatType,
                    minStartIdx, maxStartIdx, minEndIdx, maxEnd
            );
            LOG.info("[effect]{} {}-{}${}-{}", count, minStartIdx, maxStartIdx, minEndIdx, maxEnd);

            // 3.为会员增加购票记录
            DateTime now = DateTime.now();
            TicketInsertReq ticketInsertReq = new TicketInsertReq();
            ticketInsertReq.setUserId(ReqHolder.getUid());
            ticketInsertReq.setPassengerId(ReqHolder.getUid());
            ticketInsertReq.setPassengerName(tickets.get(i).getPassengerName());
            ticketInsertReq.setTrainDate(date);
            ticketInsertReq.setTrainCode(trainCode);
            ticketInsertReq.setStartStation(start);
            ticketInsertReq.setStartTime(ticket.getStartTime());
            ticketInsertReq.setEndStation(end);
            ticketInsertReq.setEndTime(ticket.getEndTime());
            ticketInsertReq.setSeatType(seatType);
            ticketInsertReq.setCarriageIndex(seat.getCarriageIndex());
            ticketInsertReq.setSeatRow(seat.getRow());
            ticketInsertReq.setSeatCol(seat.getCol());
            ticketInsertReq.setGmtCreate(now);
            ticketInsertReq.setGmtModified(now);

            ucenterFeign.insert(ticketInsertReq);
            // 4.更改订单状态为成功
            ConfirmOrder confirmOrderForUpdate = new ConfirmOrder();
            confirmOrderForUpdate.setId(confirmOrder.getId());
            confirmOrderForUpdate.setGmtModified(new Date());
            confirmOrderForUpdate.setStatus(ConfirmOrderStatusEnum.SUCCESS.getCode());
            confirmOrderMapper.updateByPrimaryKeySelective(confirmOrderForUpdate);

        }

    }

}