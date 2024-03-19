package com.mnus.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.EnumUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mnus.business.domain.*;
import com.mnus.business.enums.ConfirmOrderStatusEnum;
import com.mnus.business.enums.SeatTypeEnum;
import com.mnus.business.mapper.ConfirmOrderMapper;
import com.mnus.business.mapper.DailyTrainSeatMapper;
import com.mnus.business.req.ConfirmOrderQueryReq;
import com.mnus.business.req.ConfirmOrderSaveReq;
import com.mnus.business.req.ConfirmOrderSubmitReq;
import com.mnus.business.req.ConfirmOrderTicketReq;
import com.mnus.business.resp.ConfirmOrderQueryResp;
import com.mnus.common.context.ReqHolder;
import com.mnus.common.enums.BaseErrorCodeEnum;
import com.mnus.common.exception.BizException;
import com.mnus.common.req.EntityDeleteReq;
import com.mnus.common.resp.PageResp;
import com.mnus.common.utils.IdGenUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
@Service
public class AfterConfirmOrderService {
    private static final Logger LOG = LoggerFactory.getLogger(AfterConfirmOrderService.class);
    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;

    /**
     * 根据选中的座位列表更新座位售卖情况
     *
     * @param chosenSeatList 选中的座位列表
     */
    @Transactional
    public void afterSubmit(List<DailyTrainSeat> chosenSeatList) {
        for (DailyTrainSeat seat : chosenSeatList) {
            DailyTrainSeat record = new DailyTrainSeat();
            record.setId(seat.getId());
            record.setSell(seat.getSell());
            record.setGmtModified(new Date());
            dailyTrainSeatMapper.updateByPrimaryKeySelective(record);
        }
    }

}