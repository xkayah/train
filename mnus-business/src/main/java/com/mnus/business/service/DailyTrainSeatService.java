package com.mnus.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnus.business.domain.DailyTrainSeat;
import com.mnus.business.domain.DailyTrainSeatExample;
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
            //if (!Objects.equals(ReqHolder.getUid(), req.getUserId())) {
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
        //Long uid = req.getUserId();
        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        //if (Objects.nonNull(uid)) {
        //    dailyTrainSeatExample.createCriteria().andUserIdEqualTo(uid);
        //}
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

}