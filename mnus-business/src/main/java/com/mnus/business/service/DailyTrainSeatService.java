package com.mnus.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnus.business.domain.*;
import com.mnus.business.mapper.DailyTrainSeatMapper;
import com.mnus.business.req.DailyTrainSeatQueryReq;
import com.mnus.business.req.DailyTrainSeatSaveReq;
import com.mnus.business.resp.DailyTrainSeatQueryResp;
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
public class DailyTrainSeatService {
    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainSeatService.class);
    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;
    @Resource
    private TrainSeatService trainSeatService;

    @Resource
    private TrainStationService trainStationService;

    public void save(DailyTrainSeatSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(req, DailyTrainSeat.class);
        // notnull,insert
        if (Objects.isNull(dailyTrainSeat.getId())) {
            // todo 保存之前，先校验唯一键是否存在
            dailyTrainSeat.setId(IdGenUtil.nextId());
            dailyTrainSeat.setGmtCreate(now);
            dailyTrainSeat.setGmtModified(now);
            dailyTrainSeatMapper.insert(dailyTrainSeat);
        } else {
            // null,update
            // if (!Objects.equals(ReqHolder.getUid(), req.getUserId())) {
            //    throw new BizException(BaseErrorCodeEnum.SYSTEM_USER_CANNOT_UPDATE_OTHER_USER);
            //}
            dailyTrainSeat.setGmtModified(now);
            dailyTrainSeatMapper.updateByPrimaryKeySelective(dailyTrainSeat);
        }
    }

    public void delete(EntityDeleteReq req) {
        // todo 是否是该用户的数据
        dailyTrainSeatMapper.deleteByPrimaryKey(req.getId());
    }

    public PageResp<DailyTrainSeatQueryResp> queryList(DailyTrainSeatQueryReq req) {
        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        dailyTrainSeatExample.setOrderByClause("date desc, train_code asc, carriage_seat_index asc");
        String trainCode = req.getTrainCode();
        if (Objects.nonNull(trainCode)) {
            dailyTrainSeatExample.createCriteria()
                    .andTrainCodeEqualTo(trainCode);
        }
        // 分页请求
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<DailyTrainSeat> dailyTrainSeatList = dailyTrainSeatMapper.selectByExample(dailyTrainSeatExample);
        // 获取分页
        PageInfo<DailyTrainSeat> pageInfo = new PageInfo<>(dailyTrainSeatList);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        // 封装分页
        List<DailyTrainSeatQueryResp> list = BeanUtil.copyToList(pageInfo.getList(), DailyTrainSeatQueryResp.class);
        PageResp<DailyTrainSeatQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(total);
        pageResp.setList(list);
        pageResp.setPages(pages);
        LOG.info("[query] pageNo:{},pageSize:{},total:{},pages:{}",
                req.getPageNo(), req.getPageSize(), total, pages);
        return pageResp;
    }

    /**
     * 生成某日期下所有的【日常车厢】信息
     *
     * @param date
     */
    public void genDaily(Date date, String trainCode) {
        // 删除该【日期】该【车次】下的所有【日常车厢】信息
        DailyTrainSeatExample example = new DailyTrainSeatExample();
        example.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andDateEqualTo(date);
        dailyTrainSeatMapper.deleteByExample(example);
        // 查某【车次】的所有【车厢】信息
        List<TrainSeat> trainCarriageList = trainSeatService.selectByTrainCode(trainCode);
        LOG.info("[Seat]list size:{}", trainCarriageList.size());
        if (CollUtil.isEmpty(trainCarriageList)) {
            return;
        }
        // 查询出该车次的车站数.例如,有五个站:A B C D E,则 sell = "0000"
        int stationCount = trainStationService.countTrainStation(trainCode);
        String sell = StrUtil.fillBefore("", '0', stationCount - 1);
        for (TrainSeat trainSeat : trainCarriageList) {
            // 生成该【车次】该【日期】下的【日常车厢】信息
            DateTime now = DateTime.now();
            DailyTrainSeat record = BeanUtil.copyProperties(trainSeat, DailyTrainSeat.class);
            record.setId(IdGenUtil.nextId());
            record.setDate(date);
            record.setGmtCreate(now);
            record.setGmtModified(now);
            // 注意，要查询车站信息
            record.setSell(sell);
            dailyTrainSeatMapper.insert(record);
            LOG.info("[seat]row:{}, col:{}, offsetIdx:{}",
                    trainSeat.getRow(), trainSeat.getCol(), trainSeat.getCarriageSeatIndex());
        }
    }

    /**
     * 计算座位数量，座位数为 0 返回 -1
     */
    public int countSeat(Date date, String trainCode, String seatType) {
        DailyTrainSeatExample example = new DailyTrainSeatExample();
        example.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andDateEqualTo(date)
                .andSeatTypeEqualTo(seatType);
        int count = (int) dailyTrainSeatMapper.countByExample(example);
        return count == 0 ? -1 : count;
    }
}