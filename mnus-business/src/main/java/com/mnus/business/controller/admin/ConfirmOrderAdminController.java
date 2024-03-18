package com.mnus.business.controller.admin;

import com.mnus.common.context.ReqHolder;
import com.mnus.common.req.EntityDeleteReq;
import com.mnus.common.resp.CommonResp;
import com.mnus.common.resp.PageResp;
import com.mnus.business.req.ConfirmOrderQueryReq;
import com.mnus.business.req.ConfirmOrderSaveReq;
import com.mnus.business.resp.ConfirmOrderQueryResp;
import com.mnus.business.service.ConfirmOrderService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
@RestController
@RequestMapping("/admin/confirm-order")
public class ConfirmOrderAdminController {
    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody ConfirmOrderSaveReq req) {
        confirmOrderService.save(req);
        return CommonResp.success();
    }

    @DeleteMapping("/delete")
    public CommonResp<Object> delete(@Valid EntityDeleteReq req) {
        confirmOrderService.delete(req);
        return CommonResp.success();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<ConfirmOrderQueryResp>> queryList(@Valid ConfirmOrderQueryReq req) {
        PageResp<ConfirmOrderQueryResp> list = confirmOrderService.queryList(req);
        return CommonResp.success(list);
    }
}