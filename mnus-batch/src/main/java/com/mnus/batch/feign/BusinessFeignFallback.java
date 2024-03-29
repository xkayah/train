package com.mnus.batch.feign;

import com.mnus.common.resp.CommonResp;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/29 23:30:46
 */
@Component
public class BusinessFeignFallback implements BusinessFeign{
    @Override
    public String hello() {
        return "hello() Fallbck";
    }

    @Override
    public CommonResp<Object> genDailyTrain(Date date) {
        return null;
    }
}
