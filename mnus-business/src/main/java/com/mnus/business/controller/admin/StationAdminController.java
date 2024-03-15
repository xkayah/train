package com.mnus.business.controller.admin;

import com.mnus.business.req.StationQueryReq;
import com.mnus.business.req.StationSaveReq;
import com.mnus.business.resp.StationQueryResp;
import com.mnus.business.service.StationService;
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
@RequestMapping("/admin/station")
public class StationAdminController {
    @Resource
    private StationService stationService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody StationSaveReq req) {
        stationService.save(req);
        return CommonResp.success();
    }

    @DeleteMapping("/delete")
    public CommonResp<Object> delete(@Valid EntityDeleteReq req) {
        stationService.delete(req);
        return CommonResp.success();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<StationQueryResp>> queryList(@Valid StationQueryReq req) {
        PageResp<StationQueryResp> list = stationService.queryList(req);
        return CommonResp.success(list);
    }
}