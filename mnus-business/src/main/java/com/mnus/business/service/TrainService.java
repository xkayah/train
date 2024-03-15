package com.mnus.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnus.common.req.EntityDeleteReq;
import com.mnus.common.resp.PageResp;
import com.mnus.common.utils.IdGenUtil;
import com.mnus.business.domain.Train;
import com.mnus.business.domain.TrainExample;
import com.mnus.business.mapper.TrainMapper;
import com.mnus.business.req.TrainQueryReq;
import com.mnus.business.req.TrainSaveReq;
import com.mnus.business.resp.TrainQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
@Service
public class TrainService {
    private static final Logger LOG = LoggerFactory.getLogger(TrainService.class);
    @Resource
    private TrainMapper trainMapper;

    public void save(TrainSaveReq req) {
        DateTime now = DateTime.now();
        Train train = BeanUtil.copyProperties(req, Train.class);
        // notnull,insert
        if (Objects.isNull(train.getId())) {
            train.setId(IdGenUtil.nextId());
            train.setGmtCreate(now);
            train.setGmtModified(now);
            trainMapper.insert(train);
        } else {
            // null,update
            train.setGmtModified(now);
            trainMapper.updateByPrimaryKeySelective(train);
        }
    }

    public void delete(EntityDeleteReq req) {
        // todo 是否是该用户的数据
        trainMapper.deleteByPrimaryKey(req.getId());
    }

    public PageResp<TrainQueryResp> queryList(TrainQueryReq req) {
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("code asc");
        String code = req.getCode();
        if (StringUtils.hasText(code)) {
            trainExample.createCriteria().
                    andCodeEqualTo(code);
        }
        // 分页请求
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<Train> trainList = trainMapper.selectByExample(trainExample);
        // 获取分页
        PageInfo<Train> pageInfo = new PageInfo<>(trainList);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        // 封装分页
        List<TrainQueryResp> list = BeanUtil.copyToList(pageInfo.getList(), TrainQueryResp.class);
        PageResp<TrainQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(total);
        pageResp.setList(list);
        pageResp.setPages(pages);
        LOG.info("[query] pageNo:{},pageSize:{},total:{},pages:{}",
                req.getPageNo(), req.getPageSize(), total, pages);
        return pageResp;
    }

}