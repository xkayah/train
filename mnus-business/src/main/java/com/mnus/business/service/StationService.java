package com.mnus.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnus.common.enums.BaseErrorCodeEnum;
import com.mnus.common.exception.BizException;
import com.mnus.common.req.EntityDeleteReq;
import com.mnus.common.resp.PageResp;
import com.mnus.common.utils.IdGenUtil;
import com.mnus.business.domain.Station;
import com.mnus.business.domain.StationExample;
import com.mnus.business.mapper.StationMapper;
import com.mnus.business.req.StationQueryReq;
import com.mnus.business.req.StationSaveReq;
import com.mnus.business.resp.StationQueryResp;
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
public class StationService {
    private static final Logger LOG = LoggerFactory.getLogger(StationService.class);
    @Resource
    private StationMapper stationMapper;

    public void save(StationSaveReq req) {
        DateTime now = DateTime.now();
        Station station = BeanUtil.copyProperties(req, Station.class);
        // notnull,insert
        if (Objects.isNull(station.getId())) {
            // 保存之前，先校验唯一键是否存在
            StationExample stationExample = new StationExample();
            stationExample.createCriteria().
                    andNameEqualTo(req.getName());
            long count = stationMapper.countByExample(stationExample);
            if (count > 0L) {
                throw new BizException(BaseErrorCodeEnum.BUSINESS_STATION_ALREADY_EXISTS);
            }
            station.setId(IdGenUtil.nextId());
            station.setGmtCreate(now);
            station.setGmtModified(now);
            stationMapper.insert(station);
        } else {
            // null,update
            station.setGmtModified(now);
            stationMapper.updateByPrimaryKeySelective(station);
        }
    }

    public void delete(EntityDeleteReq req) {
        // todo 是否是该用户的数据
        stationMapper.deleteByPrimaryKey(req.getId());
    }

    public PageResp<StationQueryResp> queryList(StationQueryReq req) {
        StationExample stationExample = new StationExample();
        // if (Objects.nonNull(uid)) {
        //     stationExample.createCriteria().andUserIdEqualTo(uid);
        // }
        // 分页请求
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<Station> stationList = stationMapper.selectByExample(stationExample);
        // 获取分页
        PageInfo<Station> pageInfo = new PageInfo<>(stationList);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        // 封装分页
        List<StationQueryResp> list = BeanUtil.copyToList(pageInfo.getList(), StationQueryResp.class);
        PageResp<StationQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(total);
        pageResp.setList(list);
        pageResp.setPages(pages);
        LOG.info("[query] pageNo:{},pageSize:{},total:{},pages:{}",
                req.getPageNo(), req.getPageSize(), total, pages);
        return pageResp;
    }

}