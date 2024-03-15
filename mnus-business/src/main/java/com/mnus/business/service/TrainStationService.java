package com.mnus.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnus.common.context.ReqHolder;
import com.mnus.common.enums.BaseErrorCodeEnum;
import com.mnus.common.exception.BizException;
import com.mnus.common.req.EntityDeleteReq;
import com.mnus.common.resp.PageResp;
import com.mnus.common.utils.IdGenUtil;
import com.mnus.business.domain.TrainStation;
import com.mnus.business.domain.TrainStationExample;
import com.mnus.business.mapper.TrainStationMapper;
import com.mnus.business.req.TrainStationQueryReq;
import com.mnus.business.req.TrainStationSaveReq;
import com.mnus.business.resp.TrainStationQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
@Service
public class TrainStationService {
    private static final Logger LOG = LoggerFactory.getLogger(TrainStationService.class);
    @Resource
    private TrainStationMapper trainStationMapper;

    public void save(TrainStationSaveReq req) {
        DateTime now = DateTime.now();
        TrainStation trainStation = BeanUtil.copyProperties(req, TrainStation.class);
        // notnull,insert
        if (Objects.isNull(trainStation.getId())) {
            trainStation.setId(IdGenUtil.nextId());
            trainStation.setGmtCreate(now);
            trainStation.setGmtModified(now);
            trainStationMapper.insert(trainStation);
        } else {
            // null,update
            trainStation.setGmtModified(now);
            trainStationMapper.updateByPrimaryKeySelective(trainStation);
        }
    }

    public void delete(EntityDeleteReq req) {
        // todo 是否是该用户的数据
        trainStationMapper.deleteByPrimaryKey(req.getId());
    }

    public PageResp<TrainStationQueryResp> queryList(TrainStationQueryReq req) {
        // Long uid = req.getUserId();
        TrainStationExample trainStationExample = new TrainStationExample();
        // if (Objects.nonNull(uid)) {
        //     trainStationExample.createCriteria().andUserIdEqualTo(uid);
        // }
        // 分页请求
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<TrainStation> trainStationList = trainStationMapper.selectByExample(trainStationExample);
        // 获取分页
        PageInfo<TrainStation> pageInfo = new PageInfo<>(trainStationList);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        // 封装分页
        List<TrainStationQueryResp> list = BeanUtil.copyToList(pageInfo.getList(), TrainStationQueryResp.class);
        PageResp<TrainStationQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(total);
        pageResp.setList(list);
        pageResp.setPages(pages);
        LOG.info("[query] pageNo:{},pageSize:{},total:{},pages:{}",
                req.getPageNo(), req.getPageSize(), total, pages);
        return pageResp;
    }

}