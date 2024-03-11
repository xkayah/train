package com.mnus.ucenter.services;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.mnus.common.utils.IdGenUtil;
import com.mnus.ucenter.domain.Passenger;
import com.mnus.ucenter.mapper.PassengerMapper;
import com.mnus.ucenter.req.PassengerSaveReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
        // inset
        if (ObjectUtils.isEmpty(passenger.getId())) {
            DateTime now = DateTime.now();
            passenger.setId(IdGenUtil.nextId());
            passenger.setGmtCreate(now);
            passengerMapper.insert(passenger);
        } else {
            // update
            passengerMapper.updateByPrimaryKey(passenger);
        }
    }

}
