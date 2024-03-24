package com.mnus.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.EnumUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnus.business.domain.*;
import com.mnus.business.enums.ConfirmOrderStatusEnum;
import com.mnus.business.enums.SeatTypeEnum;
import com.mnus.business.mapper.ConfirmOrderMapper;
import com.mnus.business.req.ConfirmOrderQueryReq;
import com.mnus.business.req.ConfirmOrderSaveReq;
import com.mnus.business.req.ConfirmOrderSubmitReq;
import com.mnus.business.req.ConfirmOrderTicketReq;
import com.mnus.business.resp.ConfirmOrderQueryResp;
import com.mnus.common.context.ReqHolder;
import com.mnus.common.enums.BaseErrorCodeEnum;
import com.mnus.common.exception.BizException;
import com.mnus.common.req.EntityDeleteReq;
import com.mnus.common.resp.PageResp;
import com.mnus.common.utils.IdGenUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
@Service
public class ConfirmOrderService {
    private static final Logger LOG = LoggerFactory.getLogger(ConfirmOrderService.class);
    @Resource
    private ConfirmOrderMapper confirmOrderMapper;
    @Resource
    private DailyTrainService dailyTrainService;
    @Resource
    private DailyTrainSeatService dailyTrainSeatService;
    @Resource
    private DailyTrainStationService dailyTrainStationService;
    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;
    @Resource
    private DailyTrainTicketService dailyTrainTicketService;
    @Resource
    private AfterConfirmOrderService afterConfirmOrderService;


    public void save(ConfirmOrderSaveReq req) {
        DateTime now = DateTime.now();
        ConfirmOrder confirmOrder = BeanUtil.copyProperties(req, ConfirmOrder.class);
        // notnull,insert
        if (Objects.isNull(confirmOrder.getId())) {
            // todo 保存之前，先校验唯一键是否存在
            confirmOrder.setId(IdGenUtil.nextId());
            confirmOrder.setGmtCreate(now);
            confirmOrder.setGmtModified(now);
            confirmOrderMapper.insert(confirmOrder);
        } else {
            // null,update
            // if (!Objects.equals(ReqHolder.getUid(), req.getUserId())) {
            //    throw new BizException(BaseErrorCodeEnum.SYSTEM_USER_CANNOT_UPDATE_OTHER_USER);
            //}
            confirmOrder.setGmtModified(now);
            confirmOrderMapper.updateByPrimaryKeySelective(confirmOrder);
        }
    }

    public void delete(EntityDeleteReq req) {
        // todo 是否是该用户的数据
        confirmOrderMapper.deleteByPrimaryKey(req.getId());
    }

    public PageResp<ConfirmOrderQueryResp> queryList(ConfirmOrderQueryReq req) {
        // Long uid = req.getUserId();
        ConfirmOrderExample confirmOrderExample = new ConfirmOrderExample();
        // if (Objects.nonNull(uid)) {
        //    confirmOrderExample.createCriteria().andUserIdEqualTo(uid);
        //}
        // 分页请求
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<ConfirmOrder> confirmOrderList = confirmOrderMapper.selectByExample(confirmOrderExample);
        // 获取分页
        PageInfo<ConfirmOrder> pageInfo = new PageInfo<>(confirmOrderList);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        // 封装分页
        List<ConfirmOrderQueryResp> list = BeanUtil.copyToList(pageInfo.getList(), ConfirmOrderQueryResp.class);
        PageResp<ConfirmOrderQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(total);
        pageResp.setList(list);
        pageResp.setPages(pages);
        LOG.info("[query] pageNo:{},pageSize:{},total:{},pages:{}",
                req.getPageNo(), req.getPageSize(), total, pages);
        return pageResp;
    }

    public void doSubmit(ConfirmOrderSubmitReq req) {
        // 1.数据校验:车次、出发站、到达站
        // todo 车次是否在有效期内、tickets条数大于0、同乘客车次是否已买过
        String trainCode = req.getTrainCode();
        Date date = req.getDate();
        String start = req.getStart();
        String end = req.getEnd();
        // 车次
        int trainCount = dailyTrainService.countUnique(date, trainCode);
        if (trainCount == 0) {
            throw new BizException(BaseErrorCodeEnum.BUSINESS_ORDER_INFO_TRAIN_CODE_NOT_EXISTS);
        }
        // 出发站
        int startCount = dailyTrainStationService.countUnique(date, trainCode, start);
        if (startCount == 0) {
            throw new BizException(BaseErrorCodeEnum.BUSINESS_ORDER_INFO_START_NOT_EXISTS);
        }
        // 到达站
        int endCount = dailyTrainStationService.countUnique(date, trainCode, end);
        if (endCount == 0) {
            throw new BizException(BaseErrorCodeEnum.BUSINESS_ORDER_INFO_END_NOT_EXISTS);
        }
        // 2.保存订单信息
        List<ConfirmOrderTicketReq> tickets = req.getTickets();
        DateTime now = DateTime.now();
        ConfirmOrder record = new ConfirmOrder();
        record.setId(IdGenUtil.nextId());
        record.setUserId(ReqHolder.getUid());
        record.setDate(date);
        record.setTrainCode(trainCode);
        record.setStart(start);
        record.setEnd(end);
        record.setDailyTrainTicketId(req.getDailyTrainTicketId());
        record.setStatus(ConfirmOrderStatusEnum.INIT.getCode());// 初始状态
        record.setGmtCreate(now);
        record.setGmtModified(now);
        record.setTickets(JSON.toJSONString(tickets));
        confirmOrderMapper.insert(record);
        // 3.查询余票记录
        DailyTrainTicket ticketDB = dailyTrainTicketService.selectUnique(date, trainCode, start, end);
        // 4.预扣减余票,判断票数是否足够
        reduceTicketCount(tickets, ticketDB);
        // 5.开始选座
        //   00   ·01    02   ·03
        //   04   ·05    06    07
        List<Integer> exceptSeatList = new ArrayList<>();
        for (ConfirmOrderTicketReq ticket : tickets) {
            Integer seat = ticket.getSeat();
            if (Objects.nonNull(seat)) {
                exceptSeatList.add(seat);
            }
        }
        List<DailyTrainSeat> chosenSeatList = new ArrayList<>();
        if (CollectionUtils.isEmpty(exceptSeatList)) {
            // 无选座
            chosenSeatList = chooseSeat(date, trainCode, tickets,
                    ticketDB.getStartIndex(), ticketDB.getEndIndex());
        } else {
            // 有选座
            String seatType = tickets.get(0).getSeatTypeCode();
            chosenSeatList = chooseSeats(trainCode, date,
                    ticketDB.getStartIndex(), ticketDB.getEndIndex(), seatType,
                    exceptSeatList);
        }
        LOG.info("[exceptSeat]:{}", exceptSeatList);
        LOG.info("[chosen]:{}", chosenSeatList);
        // 6.根据选中的座位列表更新座位售卖情况
        if (Objects.isNull(chosenSeatList)) {
            throw new BizException(BaseErrorCodeEnum.BUSINESS_CHOOSE_SEAT_FAILED);
        }
        afterConfirmOrderService.afterSubmit(ticketDB,chosenSeatList,tickets);

    }

    /**
     * 随机选座
     * 此次选座的座位类型不一定是同样的,返回的座位索引所在车厢号也不一定是同一个
     *
     * @param trainCode 车次编号
     * @param date      日期
     * @param start     起始站
     * @param end       终点站
     * @param tickets   购票信息
     * @return 未找到则返回 null
     */
    private List<DailyTrainSeat> chooseSeat(Date date, String trainCode, List<ConfirmOrderTicketReq> tickets,
                                            Integer start, Integer end) {
        // chosenSeatList  保存已选择的座位
        List<DailyTrainSeat> chosenSeatList = new ArrayList<>();
        for (int i = 0; i < tickets.size(); i++) {
            LOG.info("[choose]seat#{}", i);
            ConfirmOrderTicketReq ticket = tickets.get(i);
            boolean isOk = false;// 记录当前票随机选座是否已完成 若true则可跳出循环直接开始下一张票的随机选座
            List<DailyTrainCarriage> carriageList = dailyTrainCarriageService.select(date, trainCode, ticket.getSeatTypeCode());
            for (DailyTrainCarriage carriage : carriageList) {
                List<DailyTrainSeat> seatList = dailyTrainSeatService.select(date, trainCode, ticket.getSeatTypeCode(), carriage.getIndex());
                // 一个一个座位找 找到则直接找下一张票的座位
                for (int idx = 0; idx < seatList.size(); idx++) {
                    DailyTrainSeat seat = seatList.get(idx);
                    boolean isChosen = false; // 记录当前座位是否已选择
                    if (trySell(seat.getSell(), start, end)) {
                        // 遍历已选择的列表 判断是否
                        LOG.info("[seat]#{}.row:{}, col:{}, idx:{} IN CARRIAGE{}",
                                i, seat.getRow(), seat.getCol(), idx + 1, carriage.getIndex());
                        for (DailyTrainSeat chosenSeat : chosenSeatList) {
                            if (chosenSeat.getId().equals(seat.getId())) {
                                LOG.info("[seat]#{}.idx:{} IN CARRIAGE{},but already exist.",
                                        i, idx + 1, carriage.getIndex());
                                isChosen = true;
                                break;
                            }
                        }
                        // 可以售卖且未选择过
                        if (!isChosen) {
                            // 加入列表之前更新售卖情况
                            seat.setSell(sell(seat.getSell(), start, end));
                            chosenSeatList.add(seat);
                            isOk = true;
                        }
                    }
                    if (isOk) {
                        break;
                    }
                }
                if (isOk) {
                    break;
                }
            }
            if (chosenSeatList.size() == tickets.size()) {
                return chosenSeatList;
            }
        }
        LOG.info("[choose]no seat found.");
        return null;
    }

    /**
     * 自定义选座
     * 此次选座一定都是同样的座位类型,返回的座位索引所在车厢号也一定是同一个
     *
     * @param trainCode      车次编号
     * @param date           日期
     * @param start          起始站
     * @param end            终点站
     * @param seatType       座位类型
     * @param exceptSeatList 期望选择的座位
     * @return 未找到则返回 null
     */
    private List<DailyTrainSeat> chooseSeats(String trainCode, Date date,
                                             Integer start, Integer end, String seatType,
                                             List<Integer> exceptSeatList) {
        // 预计选择失败
        boolean isOk = false;
        // chosenSeatList  保存已选择的座位
        List<DailyTrainSeat> chosenSeatList = new ArrayList<>();
        // 车厢列表
        List<DailyTrainCarriage> carriageList = dailyTrainCarriageService.select(date, trainCode, seatType);
        // 一个一个车厢查找 先找第一个座位 再找后面的座位 若前面的座位不符合则需从第一个座位从新查找
        // 当重新查找时 只需计算偏移量与列数之和即可得到下一个位置
        for (DailyTrainCarriage carriage : carriageList) {
            int seatCount = carriage.getSeatCount();
            Integer colCount = carriage.getColCount();
            Integer rowCount = carriage.getRowCount();
            List<DailyTrainSeat> seatList = dailyTrainSeatService.select(date, trainCode, seatType, carriage.getIndex());
            // 一行一行寻找
            for (int row = 0; row < rowCount; row++) {
                chosenSeatList.clear();
                for (int i = 0; i < exceptSeatList.size(); i++) {
                    LOG.info("[choose]seat#{}", i);
                    int nextIdx = row * colCount + exceptSeatList.get(i);
                    if (nextIdx > seatCount) {
                        isOk = false;
                        break;
                    }
                    // 尝试获取后面的座位
                    if (trySell(seatList.get(nextIdx).getSell(), start, end)) {
                        DailyTrainSeat nextSeat = seatList.get(nextIdx);
                        // 加入列表之前更新售卖情况
                        nextSeat.setSell(sell(nextSeat.getSell(), start, end));
                        chosenSeatList.add(nextSeat);
                        LOG.info("[seat]#{}.row:{}, col:{}, idx:{} IN CARRIAGE{}",
                                i, nextSeat.getRow(), nextSeat.getCol(), nextIdx + 1, carriage.getIndex());
                    } else {
                        // 获取失败,直接找下一行
                        isOk = false;
                        break;
                    }
                    // 以最后一个期望座位为准 如果最后一个成功获取了就可返回
                    if (i == exceptSeatList.size() - 1) {
                        isOk = true;
                    }
                }
                // 所有座位都选完
                if (isOk) {
                    return chosenSeatList;
                }
            }
            LOG.info("[choose]no seat found in {},next carriage.", carriage.getIndex());
        }
        LOG.info("[choose]no seat found.");
        return null;
    }

    /**
     * 尝试售卖座位
     *
     * @param sold     已售详情
     * @param startIdx 起始索引
     * @param endIdx   终点索引
     */
    private boolean trySell(String sold, Integer startIdx, Integer endIdx) {
        // 10001 1,3 -> 000
        String toSell = sold.substring(startIdx, endIdx);
        return Integer.valueOf(toSell, 2) == 0;
    }

    /**
     * 一般先通过
     * {@link com.mnus.business.service.ConfirmOrderService#trySell(java.lang.String, java.lang.Integer, java.lang.Integer)}
     * 判断能否售卖该座位,再调用此方法计算购买该座位后的销售详情
     *
     * @param sold     已售详情
     * @param startIdx 起始索引
     * @param endIdx   终点索引
     */
    private String sell(String sold, Integer startIdx, Integer endIdx) {
        int x = Integer.valueOf(sold, 2);
        // endIdx - startIdx -> 生成1的数量
        // ~(-0b1 << endIdx - startIdx) -> 利用负数反码的特性生成111...
        // sold.length() - endIdx) -> 偏移的位数,后缀补零
        int y = ~(-0b1 << endIdx - startIdx) << (sold.length() - endIdx);
        // x|y -> result
        StringBuilder sell = new StringBuilder(Integer.toBinaryString(x | y));
        // 前缀补零
        int preCount = sold.length() - sell.length();
        for (int i = 0; i < preCount; i++) {
            sell.insert(0, "0");
        }
        LOG.info("{}|{}", Integer.toBinaryString(x), Integer.toBinaryString(y));
        return sell.toString();
    }

    private static void reduceTicketCount(List<ConfirmOrderTicketReq> tickets, DailyTrainTicket ticketDB) {
        for (ConfirmOrderTicketReq ticket : tickets) {
            SeatTypeEnum anEnum = EnumUtil.getBy(SeatTypeEnum::getCode, ticket.getSeatTypeCode());
            switch (anEnum) {
                case YDZ -> {
                    int left = ticketDB.getYdz();
                    if (left < 0) {
                        throw new BizException(BaseErrorCodeEnum.BUSINESS_ORDER_INFO_YDZ_NOT_EXISTS);
                    }
                    left = left - 1;
                    if (left < 0) {
                        throw new BizException(BaseErrorCodeEnum.BUSINESS_TICKET_LEFT_ZERO);
                    }
                    ticketDB.setYdz(left);
                }
                case EDZ -> {
                    int left = ticketDB.getEdz();
                    if (left < 0) {
                        throw new BizException(BaseErrorCodeEnum.BUSINESS_ORDER_INFO_EDZ_NOT_EXISTS);
                    }
                    left = left - 1;
                    if (left < 0) {
                        throw new BizException(BaseErrorCodeEnum.BUSINESS_TICKET_LEFT_ZERO);
                    }
                    ticketDB.setEdz(left);
                }
                case RW -> {
                    int left = ticketDB.getRw();
                    if (left < 0) {
                        throw new BizException(BaseErrorCodeEnum.BUSINESS_ORDER_INFO_RW_NOT_EXISTS);
                    }
                    left = left - 1;
                    if (left < 0) {
                        throw new BizException(BaseErrorCodeEnum.BUSINESS_TICKET_LEFT_ZERO);
                    }
                    ticketDB.setRw(left);
                }
                case YW -> {
                    int left = ticketDB.getYw();
                    if (left < 0) {
                        throw new BizException(BaseErrorCodeEnum.BUSINESS_ORDER_INFO_YW_NOT_EXISTS);
                    }
                    left = left - 1;
                    if (left < 0) {
                        throw new BizException(BaseErrorCodeEnum.BUSINESS_TICKET_LEFT_ZERO);
                    }
                    ticketDB.setYw(left);
                }
            }

        }
    }

}