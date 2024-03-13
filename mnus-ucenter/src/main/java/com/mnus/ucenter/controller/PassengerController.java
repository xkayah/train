package com.mnus.ucenter.controller;

import com.mnus.common.context.ReqHolder;
import com.mnus.common.resp.CommonResp;
import com.mnus.ucenter.req.PassengerQueryReq;
import com.mnus.ucenter.req.PassengerSaveReq;
import com.mnus.ucenter.resp.PassengerQueryResp;
import com.mnus.ucenter.services.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/11 12:27:44
 */
@RestController
@RequestMapping("/passenger")
public class PassengerController {
    @Resource
    private PassengerService passengerService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody PassengerSaveReq req) {
        // 为了使service通用，将这个uid设置放到controller
        req.setUserId(ReqHolder.getUid());
        passengerService.save(req);
        return CommonResp.success();
    }

    @GetMapping("/query-list")
    public CommonResp<Object> queryList(@Valid PassengerQueryReq req) {
        // 为了使service通用，将这个uid设置放到controller
        req.setUserId(ReqHolder.getUid());
        List<PassengerQueryResp> list = passengerService.queryList(req);
        return CommonResp.success(list);
    }
}