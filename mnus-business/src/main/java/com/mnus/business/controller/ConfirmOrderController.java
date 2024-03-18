package com.mnus.business.controller;

import com.mnus.business.req.ConfirmOrderQueryReq;
import com.mnus.business.req.ConfirmOrderSaveReq;
import com.mnus.business.req.ConfirmOrderSubmitReq;
import com.mnus.business.resp.ConfirmOrderQueryResp;
import com.mnus.business.service.ConfirmOrderService;
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