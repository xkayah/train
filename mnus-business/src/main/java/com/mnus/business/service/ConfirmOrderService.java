package com.mnus.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.EnumUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnus.business.domain.ConfirmOrder;
import com.mnus.business.domain.ConfirmOrderExample;
import com.mnus.business.domain.DailyTrainTicket;
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
    private DailyTrainStationService dailyTrainStationService;
    @Resource
    private DailyTrainTicketService dailyTrainTicketService;

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
        // 5.选座
        // 从 idx=1 的车厢开始选座,保证座位都是在同一个车厢内
        // 6.选中座位后进入事务
        // 修改售卖情况sell
        // 为会员增加购票记录
        // 更改订单状态为成功

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