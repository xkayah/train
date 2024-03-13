package com.mnus.${module}.controller;

import com.mnus.common.context.ReqHolder;
import com.mnus.common.req.EntityDeleteReq;
import com.mnus.common.resp.CommonResp;
import com.mnus.common.resp.PageResp;
import com.mnus.${module}.req.${Domain}QueryReq;
import com.mnus.${module}.req.${Domain}SaveReq;
import com.mnus.${module}.resp.${Domain}QueryResp;
import com.mnus.${module}.service.${Domain}Service;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 */
@RestController
@RequestMapping("/${do_main}")
public class ${Domain}Controller {
    @Resource
    private ${Domain}Service ${domain}Service;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody ${Domain}SaveReq req) {
        // 为了使service通用，将这个uid设置放到controller
        req.setUserId(ReqHolder.getUid());
        ${domain}Service.save(req);
        return CommonResp.success();
    }

    @DeleteMapping("/delete")
    public CommonResp<Object> delete(@Valid EntityDeleteReq req) {
        ${domain}Service.delete(req);
        return CommonResp.success();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<${Domain}QueryResp>> queryList(@Valid ${Domain}QueryReq req) {
        // 为了使service通用，将这个uid设置放到controller
        req.setUserId(ReqHolder.getUid());
        PageResp<${Domain}QueryResp> list = ${domain}Service.queryList(req);
        return CommonResp.success(list);
    }
}