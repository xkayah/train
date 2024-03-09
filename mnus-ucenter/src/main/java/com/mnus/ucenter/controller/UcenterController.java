package com.mnus.ucenter.controller;

import com.mnus.common.resp.CommonResp;
import com.mnus.ucenter.req.UserRegistryReq;
import com.mnus.ucenter.services.UcenterService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/8 16:59:25
 */
@RestController
@RequestMapping("/user")
public class UcenterController {

    @Resource
    private UcenterService ucenterService;

    @GetMapping("/count")
    public CommonResp<Long> count() {
        return CommonResp.success(ucenterService.count());
    }

    @PostMapping("/registry")
    public CommonResp<Long> registry(UserRegistryReq req) {
        return CommonResp.success(ucenterService.registry(req));
    }

}
