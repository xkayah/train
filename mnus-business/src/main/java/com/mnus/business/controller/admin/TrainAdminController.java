package com.mnus.business.controller.admin;

import com.mnus.business.req.GenTrainSeatReq;
import com.mnus.common.context.ReqHolder;
import com.mnus.common.req.EntityDeleteReq;
import com.mnus.common.resp.CommonResp;
import com.mnus.common.resp.PageResp;
import com.mnus.business.req.TrainQueryReq;
import com.mnus.business.req.TrainSaveReq;
import com.mnus.business.resp.TrainQueryResp;
import com.mnus.business.service.TrainService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
@RestController
@RequestMapping("/admin/train")
public class TrainAdminController {
    @Resource
    private TrainService trainService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TrainSaveReq req) {
        trainService.save(req);
        return CommonResp.success();
    }

    @DeleteMapping("/delete")
    public CommonResp<Object> delete(@Valid EntityDeleteReq req) {
        trainService.delete(req);
        return CommonResp.success();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TrainQueryResp>> queryList(@Valid TrainQueryReq req) {
        PageResp<TrainQueryResp> list = trainService.queryList(req);
        return CommonResp.success(list);
    }

    @GetMapping("/gen-seat")
    public CommonResp<Object> genSeat(@Valid GenTrainSeatReq req) {
        trainService.genTrainSeat(req);
        return CommonResp.success();
    }
}