package com.mnus.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnus.business.domain.*;
import com.mnus.business.enums.SeatColEnum;
import com.mnus.business.mapper.TrainMapper;
import com.mnus.business.mapper.TrainSeatMapper;
import com.mnus.business.req.GenTrainSeatReq;
import com.mnus.business.req.TrainQueryReq;
import com.mnus.business.req.TrainSaveReq;
import com.mnus.business.resp.TrainQueryResp;
import com.mnus.common.enums.BaseErrorCodeEnum;
import com.mnus.common.exception.BizException;
import com.mnus.common.req.EntityDeleteReq;
import com.mnus.common.resp.PageResp;
import com.mnus.common.utils.IdGenUtil;
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

    @Resource
    private TrainSeatMapper trainSeatMapper;

    @Resource
    private TrainCarriageService trainCarriageService;

    public void save(TrainSaveReq req) {
        DateTime now = DateTime.now();
        Train train = BeanUtil.copyProperties(req, Train.class);
        // notnull,insert
        if (Objects.isNull(train.getId())) {
            // 保存之前，先校验唯一键是否存在
            long count = countUnique(req.getCode());
            if (count > 0L) {
                throw new BizException(BaseErrorCodeEnum.BUSINESS_TRAIN_CODE_ALREADY_EXISTS);
            }
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

    private long countUnique(String trainCode) {
        TrainExample trainExample = new TrainExample();
        trainExample.createCriteria().
                andCodeEqualTo(trainCode);
        return trainMapper.countByExample(trainExample);
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

    public void genTrainSeat(GenTrainSeatReq req) {
        DateTime now = DateTime.now();
        // 先删除后生成
        String trainCode = req.getTrainCode();
        if (!StringUtils.hasText(trainCode)) {
            throw new BizException(BaseErrorCodeEnum.REQ_PARAMS_NOT_VALID);
        }
        // 删除【车次】下的所有【座位】
        TrainSeatExample trainSeatExample = new TrainSeatExample();
        trainSeatExample.createCriteria().
                andTrainCodeEqualTo(trainCode);
        trainSeatMapper.deleteByExample(trainSeatExample);
        // 查询【车次】下的所有【车厢】
        List<TrainCarriage> trainCarriageList = trainCarriageService.selectByTrainCode(trainCode);
        // 循环生成每个【车厢】的【座位】
        for (TrainCarriage trainCarriage : trainCarriageList) {
            Integer rowCount = trainCarriage.getRowCount();
            String seatType = trainCarriage.getSeatType();
            // 通过【座位类型】拿到【车厢列】
            List<SeatColEnum> colEnumList = SeatColEnum.getColsByType(seatType);
            // 记录某个【车厢】内的【座位】索引
            int seatIdx = 1;
            // 循环【车厢行】
            for (int row = 1; row <= rowCount; row++) {
                // 循环【车厢列】
                for (SeatColEnum seatColEnum : colEnumList) {
                    TrainSeat trainSeat = new TrainSeat();
                    trainSeat.setId(IdGenUtil.nextId());
                    trainSeat.setTrainCode(trainCode);
                    trainSeat.setCarriageIndex(trainCarriage.getIndex());
                    trainSeat.setRow(StrUtil.fillBefore(String.valueOf(row), '0', 2));
                    trainSeat.setCol(seatColEnum.getCode());
                    trainSeat.setSeatType(seatType);
                    trainSeat.setCarriageSeatIndex(seatIdx++);
                    trainSeat.setGmtCreate(now);
                    trainSeat.setGmtModified(now);
                    trainSeatMapper.insert(trainSeat);
                }
            }
            LOG.info("Generate..[{}-{}], type:{}, count:{}",
                    trainCode, trainCarriage.getIndex(), seatType, seatIdx - 1);
        }
    }
}