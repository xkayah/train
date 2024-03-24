package com.mnus.ucenter.controller;

import com.mnus.common.req.EntityDeleteReq;
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
@RequestMapping("/ticket")
public class TicketController {
    @Resource
    private TicketService ticketService;

    @GetMapping("/query-list")
    public CommonResp<PageResp<TicketQueryResp>> queryList(@Valid TicketQueryReq req) {
        PageResp<TicketQueryResp> list = ticketService.queryList(req);
        return CommonResp.success(list);
    }
}