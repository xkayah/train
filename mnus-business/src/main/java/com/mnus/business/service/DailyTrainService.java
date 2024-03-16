package com.mnus.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnus.business.domain.DailyTrain;
import com.mnus.business.domain.DailyTrainExample;
import com.mnus.business.domain.Train;
import com.mnus.business.mapper.DailyTrainMapper;
import com.mnus.business.req.DailyTrainQueryReq;
import com.mnus.business.req.DailyTrainSaveReq;
import com.mnus.business.resp.DailyTrainQueryResp;
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
public class DailyTrainService {
    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainService.class);
    @Resource
    private DailyTrainMapper dailyTrainMapper;

    @Resource
    private TrainService trainService;

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    public void save(DailyTrainSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(req, DailyTrain.class);
        // notnull,insert
        if (Objects.isNull(dailyTrain.getId())) {
            // todo 保存之前，先校验唯一键是否存在
            dailyTrain.setId(IdGenUtil.nextId());
            dailyTrain.setGmtCreate(now);
            dailyTrain.setGmtModified(now);
            dailyTrainMapper.insert(dailyTrain);
        } else {
            // null,update
            // if (!Objects.equals(ReqHolder.getUid(), req.getUserId())) {
            //    throw new BizException(BaseErrorCodeEnum.SYSTEM_USER_CANNOT_UPDATE_OTHER_USER);
            //}
            dailyTrain.setGmtModified(now);
            dailyTrainMapper.updateByPrimaryKeySelective(dailyTrain);
        }
    }

    public void delete(EntityDeleteReq req) {
        // todo 是否是该用户的数据
        dailyTrainMapper.deleteByPrimaryKey(req.getId());
    }

    public PageResp<DailyTrainQueryResp> queryList(DailyTrainQueryReq req) {
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        DailyTrainExample.Criteria criteria = dailyTrainExample.createCriteria();
        String trainCode = req.getTrainCode();
        Date date = req.getDate();
        if (Objects.nonNull(trainCode)) {
            criteria
                    .andCodeEqualTo(trainCode);
        }
        if (Objects.nonNull(date)) {
            criteria
                    .andDateEqualTo(date);
        }
        // 分页请求
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<DailyTrain> dailyTrainList = dailyTrainMapper.selectByExample(dailyTrainExample);
        // 获取分页
        PageInfo<DailyTrain> pageInfo = new PageInfo<>(dailyTrainList);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        // 封装分页
        List<DailyTrainQueryResp> list = BeanUtil.copyToList(pageInfo.getList(), DailyTrainQueryResp.class);
        PageResp<DailyTrainQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(total);
        pageResp.setList(list);
        pageResp.setPages(pages);
        LOG.info("[query] pageNo:{},pageSize:{},total:{},pages:{}",
                req.getPageNo(), req.getPageSize(), total, pages);
        return pageResp;
    }

    /**
     * 生成某日期下所有的【日常车次】信息
     *
     * @param date
     */
    public void genDaily(Date date) {
        // 查询所有【车次】信息
        List<Train> trainList = trainService.selectAll();
        LOG.info("[GenDailyTrain]date:{}, list size:{}", DateTime.of(date), trainList.size());
        if (CollUtil.isEmpty(trainList)) {
            return;
        }
        for (Train train : trainList) {
            // 生成改车次数据
            genOneDaily(date, train);
            // 生成该车次的车站数据
            dailyTrainStationService.genDaily(date, train.getCode());
        }
    }

    /**
     * 生成某日期某车次的【日常车次】信息
     *
     * @param date
     * @param train
     */
    public void genOneDaily(Date date, Train train) {
        // 删除该【日期】该【车次】下的所有【日常车次】信息
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        DailyTrainExample.Criteria criteria = dailyTrainExample.createCriteria();
        criteria
                .andCodeEqualTo(train.getCode())
                .andDateEqualTo(date);
        dailyTrainMapper.deleteByExample(dailyTrainExample);
        // 生成该【日期】下的【日常车次】信息
        DateTime now = DateTime.now();
        DailyTrain record = BeanUtil.copyProperties(train, DailyTrain.class);
        record.setId(IdGenUtil.nextId());
        record.setDate(date);
        record.setGmtCreate(now);
        record.setGmtModified(now);
        dailyTrainMapper.insert(record);
        LOG.info("[train]code:{}, start:{}, end:{}",
                train.getCode(), train.getStart(), train.getEnd());
    }

}