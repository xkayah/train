package com.mnus.ucenter.controller;

import com.mnus.common.resp.CommonResp;
import com.mnus.ucenter.req.LoginOrRegistryReq;
import com.mnus.ucenter.req.UserSendCodeReq;
import com.mnus.ucenter.resp.UserLoginResp;
import com.mnus.ucenter.services.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/8 16:59:25
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/count")
    public CommonResp<Long> count() {
        return CommonResp.success(userService.count());
    }

    @PostMapping("/sign-in")
    public CommonResp<UserLoginResp> signIn(@Valid @RequestBody LoginOrRegistryReq req) {
        return CommonResp.success(userService.login(req));
    }

    @PostMapping("/send-code")
    public CommonResp<Object> sendCode(@Valid @RequestBody UserSendCodeReq req) {
        userService.sendCode(req);
        return CommonResp.success();
    }

}
