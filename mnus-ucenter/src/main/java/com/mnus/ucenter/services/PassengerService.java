package com.mnus.ucenter.services;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.mnus.common.utils.IdGenUtil;
import com.mnus.ucenter.domain.Passenger;
import com.mnus.ucenter.domain.PassengerExample;
import com.mnus.ucenter.mapper.PassengerMapper;
import com.mnus.ucenter.req.PassengerQueryReq;
import com.mnus.ucenter.req.PassengerSaveReq;
import com.mnus.ucenter.resp.PassengerQueryResp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/11 12:15:59
 */
@Service
public class PassengerService {
    @Resource
    private PassengerMapper passengerMapper;

    public void save(PassengerSaveReq req) {
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        // insert
        if (Objects.nonNull(passenger.getUserId())) {
            DateTime now = DateTime.now();
            passenger.setId(IdGenUtil.nextId());
            passenger.setGmtCreate(now);
            passenger.setGmtModified(now);
            passengerMapper.insert(passenger);
        } else {
            // update
            passengerMapper.updateByPrimaryKey(passenger);
        }
    }

    public List<PassengerQueryResp> queryList(PassengerQueryReq req) {
        Long uid = req.getUserId();
        PassengerExample passengerExample = new PassengerExample();
        if (Objects.nonNull(uid)) {
            passengerExample.createCriteria().andUserIdEqualTo(uid);
        }
        List<Passenger> passengerList = passengerMapper.selectByExample(passengerExample);
        return BeanUtil.copyToList(passengerList, PassengerQueryResp.class);
    }

}
