package com.mnus.ucenter.controller;

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
    public Long count() {
        return ucenterService.count();
    }

    @PostMapping("/registry")
    public Long registry(String mobile) {
        return ucenterService.registry(mobile);
    }

}
