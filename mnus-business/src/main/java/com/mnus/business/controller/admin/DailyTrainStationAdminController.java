package com.mnus.business.controller.admin;

import com.mnus.business.req.DailyTrainStationQueryReq;
import com.mnus.business.req.DailyTrainStationSaveReq;
import com.mnus.business.resp.DailyTrainStationQueryResp;
import com.mnus.business.service.DailyTrainStationService;
import com.mnus.common.req.EntityDeleteReq;
import com.mnus.common.resp.CommonResp;
import com.mnus.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
@RestController
@RequestMapping("/admin/daily-train-station")
public class DailyTrainStationAdminController {
    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody DailyTrainStationSaveReq req) {
        dailyTrainStationService.save(req);
        return CommonResp.success();
    }

    @DeleteMapping("/delete")
    public CommonResp<Object> delete(@Valid EntityDeleteReq req) {
        dailyTrainStationService.delete(req);
        return CommonResp.success();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainStationQueryResp>> queryList(@Valid DailyTrainStationQueryReq req) {
        PageResp<DailyTrainStationQueryResp> list = dailyTrainStationService.queryList(req);
        return CommonResp.success(list);
    }
}