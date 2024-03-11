package com.mnus.ucenter.controller;

import com.mnus.common.resp.CommonResp;
import com.mnus.ucenter.req.PassengerSaveReq;
import com.mnus.ucenter.services.PassengerService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        passengerService.save(req);
        return CommonResp.success();
    }
}
