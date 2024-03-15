package com.mnus.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnus.business.domain.DailyTrain;
import com.mnus.business.domain.DailyTrainExample;
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
            //if (!Objects.equals(ReqHolder.getUid(), req.getUserId())) {
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
        //Long uid = req.getUserId();
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        //if (Objects.nonNull(uid)) {
        //    dailyTrainExample.createCriteria().andUserIdEqualTo(uid);
        //}
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

}