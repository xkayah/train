package com.mnus.batch.feign;

import com.mnus.common.resp.CommonResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/16 11:38:52
 */
@FeignClient(name = "business", url = "http://127.0.0.1:8101/business")
public interface BusinessFeign {

    @GetMapping("/hello")
    String hello();

    @GetMapping("/admin/daily-train/gen-daily/{date}")
    CommonResp<Object> genDailyTrain(
            @PathVariable
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date date);
}
