package com.mnus.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.EnumUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnus.business.domain.*;
import com.mnus.business.enums.SeatTypeEnum;
import com.mnus.business.enums.TrainTypeEnum;
import com.mnus.common.req.EntityDeleteReq;
import com.mnus.common.resp.PageResp;
import com.mnus.common.utils.IdGenUtil;
import com.mnus.business.mapper.DailyTrainTicketMapper;
import com.mnus.business.req.DailyTrainTicketQueryReq;
import com.mnus.business.req.DailyTrainTicketSaveReq;
import com.mnus.business.resp.DailyTrainTicketQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
@Service
public class DailyTrainTicketService {
    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainTicketService.class);
    @Resource
    private DailyTrainTicketMapper dailyTrainTicketMapper;
    @Resource
    private DailyTrainSeatService dailyTrainSeatService;
    @Resource
    private TrainStationService trainStationService;

    public void save(DailyTrainTicketSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(req, DailyTrainTicket.class);
        // notnull,insert
        if (Objects.isNull(dailyTrainTicket.getId())) {
            // todo 保存之前，先校验唯一键是否存在
            dailyTrainTicket.setId(IdGenUtil.nextId());
            dailyTrainTicket.setGmtCreate(now);
            dailyTrainTicket.setGmtModified(now);
            dailyTrainTicketMapper.insert(dailyTrainTicket);
        } else {
            // null,update
            // if (!Objects.equals(ReqHolder.getUid(), req.getUserId())) {
            //    throw new BizException(BaseErrorCodeEnum.SYSTEM_USER_CANNOT_UPDATE_OTHER_USER);
            //}
            dailyTrainTicket.setGmtModified(now);
            dailyTrainTicketMapper.updateByPrimaryKeySelective(dailyTrainTicket);
        }
    }

    public void delete(EntityDeleteReq req) {
        // todo 是否是该用户的数据
        dailyTrainTicketMapper.deleteByPrimaryKey(req.getId());
    }

    public PageResp<DailyTrainTicketQueryResp> queryList(DailyTrainTicketQueryReq req) {
        // Long uid = req.getUserId();
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        // if (Objects.nonNull(uid)) {
        //    dailyTrainTicketExample.createCriteria().andUserIdEqualTo(uid);
        //}
        // 分页请求
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<DailyTrainTicket> dailyTrainTicketList = dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);
        // 获取分页
        PageInfo<DailyTrainTicket> pageInfo = new PageInfo<>(dailyTrainTicketList);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        // 封装分页
        List<DailyTrainTicketQueryResp> list = BeanUtil.copyToList(pageInfo.getList(), DailyTrainTicketQueryResp.class);
        PageResp<DailyTrainTicketQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(total);
        pageResp.setList(list);
        pageResp.setPages(pages);
        LOG.info("[query] pageNo:{},pageSize:{},total:{},pages:{}",
                req.getPageNo(), req.getPageSize(), total, pages);
        return pageResp;
    }

    /**
     * 生成某日期下所有的【余票】信息
     */
    public void genDaily(String trainType, Date date, String trainCode) {
        // 删除该【日期】该【车次】下的所有【日常车厢】信息
        DailyTrainTicketExample example = new DailyTrainTicketExample();
        example.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andDateEqualTo(date);
        dailyTrainTicketMapper.deleteByExample(example);
        // 查某【车次】的所有所经【车站】信息
        List<TrainStation> trainStationList = trainStationService.selectByTrainCode(trainCode);
        LOG.info("[Ticket]list size:{}", trainStationList.size());
        if (CollUtil.isEmpty(trainStationList)) {
            return;
        }
        DateTime now = DateTime.now();
        // 余票.每天的座位应该都是不变的,所以在循环外面先查出来,提高性能
        int ydz = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.YDZ.getCode());
        int edz = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.EDZ.getCode());
        int rw = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.RW.getCode());
        int yw = dailyTrainSeatService.countSeat(date, trainCode, SeatTypeEnum.YW.getCode());
        for (int i = 0; i < trainStationList.size(); i++) {
            // 获取起始站
            TrainStation startStation = trainStationList.get(i);
            BigDecimal km = BigDecimal.ZERO;
            for (int j = i + 1; j < trainStationList.size(); j++) {
                // 获取下一站
                TrainStation nextStation = trainStationList.get(j);
                // 累加里程
                km = km.add(nextStation.getKm());
                // 车次系数.根据车次类型获取对应枚举类中的系数
                BigDecimal priceRate = EnumUtil.getFieldBy(TrainTypeEnum::getPriceRate, TrainTypeEnum::getCode, trainType);
                // 票价 = 里程之和 * 座位单价 * 车次类型系数
                BigDecimal ydzPrice = km.multiply(SeatTypeEnum.YDZ.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal edzPrice = km.multiply(SeatTypeEnum.EDZ.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal rwPrice = km.multiply(SeatTypeEnum.RW.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);
                BigDecimal ywPrice = km.multiply(SeatTypeEnum.YW.getPrice()).multiply(priceRate).setScale(2, RoundingMode.HALF_UP);

                DailyTrainTicket record = new DailyTrainTicket();
                record.setId(IdGenUtil.nextId());
                record.setDate(date);
                record.setTrainCode(trainCode);
                record.setStart(startStation.getName());
                record.setStartPinyin(startStation.getNamePinyin());
                record.setStartTime(startStation.getInTime());
                record.setStartIndex(startStation.getIndex());
                record.setEnd(nextStation.getName());
                record.setEndPinyin(nextStation.getNamePinyin());
                record.setEndTime(nextStation.getOutTime());
                record.setEndIndex(nextStation.getIndex());
                record.setYdz(ydz);
                record.setYdzPrice(ydzPrice);
                record.setEdz(edz);
                record.setEdzPrice(edzPrice);
                record.setRw(rw);
                record.setRwPrice(rwPrice);
                record.setYw(yw);
                record.setYwPrice(ywPrice);
                record.setGmtCreate(now);
                record.setGmtModified(now);

                dailyTrainTicketMapper.insert(record);

                LOG.info("[ticket]ydz{}-{}, edz{}-{}, rw{}-{}, yw{}-{}",
                        ydz, ydzPrice, edz, edzPrice, rw, rwPrice, yw, ywPrice);
            }
        }
    }

    public DailyTrainTicket selectUnique(Date date, String trainCode,
                                         String startStation, String endStation,
                                         String stationIdx){
        DailyTrainTicketExample example = new DailyTrainTicketExample();
        example.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        List<DailyTrainTicket> list = dailyTrainTicketMapper.selectByExample(example);
        if (CollUtil.isEmpty(list)){
            return null;
        }else {
            return list.get(0);
        }
    }
}