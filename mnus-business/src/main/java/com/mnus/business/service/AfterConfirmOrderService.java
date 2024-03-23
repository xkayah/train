package com.mnus.business.service;

import com.mnus.business.domain.DailyTrainSeat;
import com.mnus.business.domain.DailyTrainTicket;
import com.mnus.business.mapper.DailyTrainSeatMapper;
import com.mnus.business.mapper.my.MyDailyTrainTicketMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 提交之后的动作
     *
     * @param ticket         [start, end)
     * @param chosenSeatList 选中的座位列表
     */
    @Transactional
    public void afterSubmit(DailyTrainTicket ticket, List<DailyTrainSeat> chosenSeatList) {
        for (DailyTrainSeat seat : chosenSeatList) {
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
            int start = ticket.getStartIndex();
            int end = ticket.getEndIndex();
            int minStart = 0;
            int maxStart = start + 1;
            int maxEnd = chs.length;
            int minEnd = end - 1;
            // minStart = 从 start 往前找第一个1.直接从 start 开始,不要-1,否则可能会越界
            for (int i = start; i > 0; i--) {
                if (chs[i] == '1') {
                    minStart = i;
                    break;
                }
            }
            // maxEnd   = 从 end   往后找第一个1.直接从 end 开始,不要+1,否则可能会越界
            for (int i = end; i < chs.length; i++) {
                if (chs[i] == '1') {
                    maxEnd = i;
                    break;
                }
            }
            long count = myDailyTrainTicketMapper.updateCountBySell(
                    ticket.getDate(), ticket.getTrainCode(), seat.getSeatType(),
                    minStart, maxStart, minEnd, maxEnd
            );
            LOG.info("[effect]{} {}-{}${}-{}", count, minStart, maxStart, minEnd, maxEnd);
        }

        // 3.为会员增加购票记录

        // 4.更改订单状态为成功

    }

}