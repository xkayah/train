package com.mnus.ucenter.controller;

import com.mnus.common.resp.CommonResp;
import com.mnus.ucenter.req.LoginOrRegistryReq;
import com.mnus.ucenter.req.UserSendCodeReq;
import com.mnus.ucenter.resp.UserLoginResp;
import com.mnus.ucenter.services.UcenterService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
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

    @PostMapping("/login")
    public CommonResp<UserLoginResp> login(@Valid LoginOrRegistryReq req) {
        return CommonResp.success(ucenterService.login(req));
    }

    @PostMapping("/send-code")
    public CommonResp<Object> sendCode(@Valid UserSendCodeReq req) {
        ucenterService.sendCode(req);
        return CommonResp.success();
    }

}
