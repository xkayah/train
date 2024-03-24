package com.mnus.ucenter.controller.admin;

import com.mnus.common.req.EntityDeleteReq;
import com.mnus.common.req.TicketInsertReq;
import com.mnus.common.resp.CommonResp;
import com.mnus.common.resp.PageResp;
import com.mnus.ucenter.req.TicketQueryReq;
import com.mnus.ucenter.req.TicketSaveReq;
import com.mnus.ucenter.resp.TicketQueryResp;
import com.mnus.ucenter.service.TicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
@RestController
@RequestMapping("/admin/ticket")
public class TicketAdminController {
    @Resource
    private TicketService ticketService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody TicketSaveReq req) {
        ticketService.save(req);
        return CommonResp.success();
    }

    @PostMapping("/insert")
    public CommonResp<Object> insert(@Valid @RequestBody TicketInsertReq req) {
        ticketService.insert(req);
        return CommonResp.success();
    }

    @DeleteMapping("/delete")
    public CommonResp<Object> delete(@Valid EntityDeleteReq req) {
        ticketService.delete(req);
        return CommonResp.success();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<TicketQueryResp>> queryList(@Valid TicketQueryReq req) {
        PageResp<TicketQueryResp> list = ticketService.queryList(req);
        return CommonResp.success(list);
    }
}