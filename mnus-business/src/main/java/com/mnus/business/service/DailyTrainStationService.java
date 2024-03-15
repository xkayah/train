package com.mnus.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnus.business.domain.DailyTrainStation;
import com.mnus.business.domain.DailyTrainStationExample;
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
            //if (!Objects.equals(ReqHolder.getUid(), req.getUserId())) {
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
        //Long uid = req.getUserId();
        DailyTrainStationExample dailyTrainStationExample = new DailyTrainStationExample();
        //if (Objects.nonNull(uid)) {
        //    dailyTrainStationExample.createCriteria().andUserIdEqualTo(uid);
        //}
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

}