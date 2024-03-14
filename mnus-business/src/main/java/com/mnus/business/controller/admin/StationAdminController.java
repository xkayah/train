package com.mnus.business.controller.admin;

import com.mnus.common.context.ReqHolder;
import com.mnus.common.req.EntityDeleteReq;
import com.mnus.common.resp.CommonResp;
import com.mnus.common.resp.PageResp;
import com.mnus.business.req.StationQueryReq;
import com.mnus.business.req.StationSaveReq;
import com.mnus.business.resp.StationQueryResp;
import com.mnus.business.service.StationService;
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
        // 为了使service通用，将这个uid设置放到controller
        req.setUserId(ReqHolder.getUid());
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
        // 为了使service通用，将这个uid设置放到controller
        req.setUserId(ReqHolder.getUid());
        PageResp<StationQueryResp> list = stationService.queryList(req);
        return CommonResp.success(list);
    }
}