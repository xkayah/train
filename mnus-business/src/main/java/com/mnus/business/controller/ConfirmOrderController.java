package com.mnus.business.controller;

import com.mnus.business.req.ConfirmOrderSubmitReq;
import com.mnus.business.service.ConfirmOrderService;
import com.mnus.common.resp.CommonResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
@RestController
@RequestMapping("/confirm-order")
public class ConfirmOrderController {
    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/submit")
    public CommonResp<Object> submit(@Valid @RequestBody ConfirmOrderSubmitReq req) {
        confirmOrderService.doSubmit(req);
        return CommonResp.success();
    }

}