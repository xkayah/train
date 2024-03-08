package com.mnus.ucenter.services;

import com.mnus.ucenter.mapper.UcenterMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/8 16:58:29
 */
@Service
public class UcenterService {

    @Resource
    private UcenterMapper ucenterMapper;

    public int count(){
        return ucenterMapper.count();
    }
}
