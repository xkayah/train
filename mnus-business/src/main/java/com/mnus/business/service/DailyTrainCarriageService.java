package com.mnus.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnus.business.domain.DailyTrainCarriage;
import com.mnus.business.domain.DailyTrainCarriageExample;
import com.mnus.business.mapper.DailyTrainCarriageMapper;
import com.mnus.business.req.DailyTrainCarriageQueryReq;
import com.mnus.business.req.DailyTrainCarriageSaveReq;
import com.mnus.business.resp.DailyTrainCarriageQueryResp;
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
public class DailyTrainCarriageService {
    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainCarriageService.class);
    @Resource
    private DailyTrainCarriageMapper dailyTrainCarriageMapper;

    public void save(DailyTrainCarriageSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(req, DailyTrainCarriage.class);
        // notnull,insert
        if (Objects.isNull(dailyTrainCarriage.getId())) {
            // todo 保存之前，先校验唯一键是否存在
            dailyTrainCarriage.setId(IdGenUtil.nextId());
            dailyTrainCarriage.setGmtCreate(now);
            dailyTrainCarriage.setGmtModified(now);
            dailyTrainCarriageMapper.insert(dailyTrainCarriage);
        } else {
            // null,update
            //if (!Objects.equals(ReqHolder.getUid(), req.getUserId())) {
            //    throw new BizException(BaseErrorCodeEnum.SYSTEM_USER_CANNOT_UPDATE_OTHER_USER);
            //}
            dailyTrainCarriage.setGmtModified(now);
            dailyTrainCarriageMapper.updateByPrimaryKeySelective(dailyTrainCarriage);
        }
    }

    public void delete(EntityDeleteReq req) {
        // todo 是否是该用户的数据
        dailyTrainCarriageMapper.deleteByPrimaryKey(req.getId());
    }

    public PageResp<DailyTrainCarriageQueryResp> queryList(DailyTrainCarriageQueryReq req) {
        //Long uid = req.getUserId();
        DailyTrainCarriageExample dailyTrainCarriageExample = new DailyTrainCarriageExample();
        //if (Objects.nonNull(uid)) {
        //    dailyTrainCarriageExample.createCriteria().andUserIdEqualTo(uid);
        //}
        // 分页请求
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<DailyTrainCarriage> dailyTrainCarriageList = dailyTrainCarriageMapper.selectByExample(dailyTrainCarriageExample);
        // 获取分页
        PageInfo<DailyTrainCarriage> pageInfo = new PageInfo<>(dailyTrainCarriageList);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        // 封装分页
        List<DailyTrainCarriageQueryResp> list = BeanUtil.copyToList(pageInfo.getList(), DailyTrainCarriageQueryResp.class);
        PageResp<DailyTrainCarriageQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(total);
        pageResp.setList(list);
        pageResp.setPages(pages);
        LOG.info("[query] pageNo:{},pageSize:{},total:{},pages:{}",
                req.getPageNo(), req.getPageSize(), total, pages);
        return pageResp;
    }

}