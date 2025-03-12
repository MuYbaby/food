package com.muybaby.food.controller;

import com.muybaby.food.bean.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/authenticated")
    public Result testAuthenticated() {
        return Result.success("Authentication successful! This endpoint is protected.");
    }

    @GetMapping("/demo")
    public Result testDemo() {
        int i = 1 / 0;
        return Result.success("This is a demo endpoint.");
    }
}