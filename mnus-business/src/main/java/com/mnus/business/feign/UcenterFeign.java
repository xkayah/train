package com.mnus.business.feign;

import com.mnus.common.req.TicketInsertReq;
import com.mnus.common.resp.CommonResp;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/24 9:24:40
 */
@FeignClient(name = "ucenter", url = "http://127.0.0.1:8001/ucenter")
public interface UcenterFeign {


    @PostMapping("/admin/ticket/insert")
    CommonResp<Object> insert(@Valid @RequestBody TicketInsertReq req);

}
