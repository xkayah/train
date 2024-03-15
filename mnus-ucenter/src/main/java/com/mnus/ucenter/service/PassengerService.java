package com.mnus.ucenter.service;

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
import com.mnus.ucenter.domain.Passenger;
import com.mnus.ucenter.domain.PassengerExample;
import com.mnus.ucenter.mapper.PassengerMapper;
import com.mnus.ucenter.req.PassengerQueryReq;
import com.mnus.ucenter.req.PassengerSaveReq;
import com.mnus.ucenter.resp.PassengerQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/11 12:15:59
 */
@Service
public class PassengerService {
    private static final Logger LOG = LoggerFactory.getLogger(PassengerService.class);
    @Resource
    private PassengerMapper passengerMapper;

    public void save(PassengerSaveReq req) {
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        // notnull,insert
        if (Objects.isNull(passenger.getId())) {
            passenger.setId(IdGenUtil.nextId());
            passenger.setGmtCreate(now);
            passenger.setGmtModified(now);
            passengerMapper.insert(passenger);
        } else {
            // null,update
            if (!Objects.equals(ReqHolder.getUid(), req.getUserId())) {
                throw new BizException(BaseErrorCodeEnum.UCENTER_USER_CANNOT_UPDATE_OTHER_USER);
            }
            passenger.setGmtModified(now);
            passengerMapper.updateByPrimaryKeySelective(passenger);
        }
    }

    public void delete(EntityDeleteReq req) {
        // todo 只查询出uid
        Passenger passenger = passengerMapper.selectByPrimaryKey(req.getId());
        if (!Objects.equals(ReqHolder.getUid(), passenger.getUserId())) {
            throw new BizException(BaseErrorCodeEnum.UCENTER_USER_CANNOT_UPDATE_OTHER_USER);
        }
        passengerMapper.deleteByPrimaryKey(req.getId());
    }

    public PageResp<PassengerQueryResp> queryList(PassengerQueryReq req) {
        Long uid = req.getUserId();
        PassengerExample passengerExample = new PassengerExample();
        if (Objects.nonNull(uid)) {
            passengerExample.createCriteria().andUserIdEqualTo(uid);
        }
        // 分页请求
        PageHelper.startPage(req.getPageNo(), req.getPageSize());
        List<Passenger> passengerList = passengerMapper.selectByExample(passengerExample);
        // 获取分页
        PageInfo<Passenger> pageInfo = new PageInfo<>(passengerList);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        // 封装分页
        List<PassengerQueryResp> list = BeanUtil.copyToList(pageInfo.getList(), PassengerQueryResp.class);
        PageResp<PassengerQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(total);
        pageResp.setList(list);
        pageResp.setPages(pages);
        LOG.info("[query] pageNo:{},pageSize:{},total:{},pages:{}",
                req.getPageNo(), req.getPageSize(), total, pages);
        return pageResp;
    }

}
