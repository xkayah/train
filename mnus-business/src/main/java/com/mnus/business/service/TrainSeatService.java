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
import com.mnus.business.domain.TrainSeat;
import com.mnus.business.domain.TrainSeatExample;
import com.mnus.business.mapper.TrainSeatMapper;
import com.mnus.business.req.TrainSeatQueryReq;
import com.mnus.business.req.TrainSeatSaveReq;
import com.mnus.business.resp.TrainSeatQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.ToDoubleBiFunction;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
@Service
public class TrainSeatService {
    private static final Logger LOG = LoggerFactory.getLogger(TrainSeatService.class);
    @Resource
    private TrainSeatMapper trainSeatMapper;

    public void save(TrainSeatSaveReq req) {
        DateTime now = DateTime.now();
        TrainSeat trainSeat = BeanUtil.copyProperties(req, TrainSeat.class);
        // notnull,insert
        if (Objects.isNull(trainSeat.getId())) {
            trainSeat.setId(IdGenUtil.nextId());
            trainSeat.setGmtCreate(now);
            trainSeat.setGmtModified(now);
            trainSeatMapper.insert(trainSeat);
        } else {
            // null,update
            trainSeat.setGmtModified(now);
            trainSeatMapper.updateByPrimaryKeySelective(trainSeat);
        }
    }

    public void delete(EntityDeleteReq req) {
        // todo 是否是该用户的数据
        trainSeatMapper.deleteByPrimaryKey(req.getId());
    }

    public PageResp<TrainSeatQueryResp> queryList(TrainSeatQueryReq req) {
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        String trainCode = req.getTrainCode();
        if (StringUtils.hasText(trainCode)) {
            trainSeatExample.createCriteria().
                    andTrainCodeEqualTo(trainCode);
        }
        trainSeatExample.setOrderByClause("train_code asc, carriage_index asc, carriage_seat_index asc");
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<TrainSeat> trainSeatList = trainSeatMapper.selectByExample(trainSeatExample);
        // 获取分页
        PageInfo<TrainSeat> pageInfo = new PageInfo<>(trainSeatList);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        // 封装分页
        List<TrainSeatQueryResp> list = BeanUtil.copyToList(pageInfo.getList(), TrainSeatQueryResp.class);
        // todo 校验分页页数
        PageResp<TrainSeatQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(total);
        pageResp.setList(list);
        pageResp.setPages(pages);
        LOG.info("[query] pageNo:{},pageSize:{},total:{},pages:{}",
                req.getPageNo(), req.getPageSize(), total, pages);
        return pageResp;
    }

}