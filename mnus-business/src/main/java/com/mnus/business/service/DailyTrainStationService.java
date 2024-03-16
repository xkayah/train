package com.mnus.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnus.business.domain.*;
import com.mnus.business.mapper.DailyTrainStationMapper;
import com.mnus.business.req.DailyTrainStationQueryReq;
import com.mnus.business.req.DailyTrainStationSaveReq;
import com.mnus.business.resp.DailyTrainStationQueryResp;
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
public class DailyTrainStationService {
    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainStationService.class);
    @Resource
    private DailyTrainStationMapper dailyTrainStationMapper;

    @Resource
    private TrainStationService trainStationService;

    public void save(DailyTrainStationSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(req, DailyTrainStation.class);
        // notnull,insert
        if (Objects.isNull(dailyTrainStation.getId())) {
            // todo 保存之前，先校验唯一键是否存在
            dailyTrainStation.setId(IdGenUtil.nextId());
            dailyTrainStation.setGmtCreate(now);
            dailyTrainStation.setGmtModified(now);
            dailyTrainStationMapper.insert(dailyTrainStation);
        } else {
            // null,update
            // if (!Objects.equals(ReqHolder.getUid(), req.getUserId())) {
            //    throw new BizException(BaseErrorCodeEnum.SYSTEM_USER_CANNOT_UPDATE_OTHER_USER);
            //}
            dailyTrainStation.setGmtModified(now);
            dailyTrainStationMapper.updateByPrimaryKeySelective(dailyTrainStation);
        }
    }

    public void delete(EntityDeleteReq req) {
        // todo 是否是该用户的数据
        dailyTrainStationMapper.deleteByPrimaryKey(req.getId());
    }

    public PageResp<DailyTrainStationQueryResp> queryList(DailyTrainStationQueryReq req) {
        // Long uid = req.getUserId();
        DailyTrainStationExample dailyTrainStationExample = new DailyTrainStationExample();
        DailyTrainStationExample.Criteria criteria = dailyTrainStationExample.createCriteria();
        String trainCode = req.getTrainCode();
        Date date = req.getDate();
        if (Objects.nonNull(trainCode)) {
            criteria
                    .andTrainCodeEqualTo(trainCode);
        }
        if (Objects.nonNull(date)) {
            criteria
                    .andDateEqualTo(date);
        }
        // 分页请求
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<DailyTrainStation> dailyTrainStationList = dailyTrainStationMapper.selectByExample(dailyTrainStationExample);
        // 获取分页
        PageInfo<DailyTrainStation> pageInfo = new PageInfo<>(dailyTrainStationList);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        // 封装分页
        List<DailyTrainStationQueryResp> list = BeanUtil.copyToList(pageInfo.getList(), DailyTrainStationQueryResp.class);
        PageResp<DailyTrainStationQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(total);
        pageResp.setList(list);
        pageResp.setPages(pages);
        LOG.info("[query] pageNo:{},pageSize:{},total:{},pages:{}",
                req.getPageNo(), req.getPageSize(), total, pages);
        return pageResp;
    }

    /**
     * 生成某日期下所有的【日常车站】信息
     *
     * @param date
     */
    public void genDaily(Date date, String trainCode) {
        // 删除该【日期】该【车次】下的所有【日常车站】信息
        DailyTrainStationExample example = new DailyTrainStationExample();
        example.createCriteria()
                .andTrainCodeEqualTo(trainCode)
                .andDateEqualTo(date);
        dailyTrainStationMapper.deleteByExample(example);
        // 查某【车次】的所有【车站】信息
        List<TrainStation> trainStationList = trainStationService.selectByTrainCode(trainCode);
        LOG.info("[GenDailyTrainStation]list size:{}", trainStationList.size());
        if (CollUtil.isEmpty(trainStationList)) {
            return;
        }
        for (TrainStation trainStation : trainStationList) {
            // 生成该【车次】该【日期】下的【日常车站】信息
            DateTime now = DateTime.now();
            DailyTrainStation record = BeanUtil.copyProperties(trainStation, DailyTrainStation.class);
            record.setId(IdGenUtil.nextId());
            record.setDate(date);
            record.setGmtCreate(now);
            record.setGmtModified(now);
            dailyTrainStationMapper.insert(record);
            LOG.info("[train station]name:{}, idx:{}", trainStation.getName(), trainStation.getIndex());
        }
    }
}