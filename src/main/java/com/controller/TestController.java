package com.controller;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试用
 * Author:kai
 * Date:2022/9/12 21:57
 */
@Slf4j
@RestController
@EnableKnife4j
@Api(value = "测试接口")
public class TestController {

    @ApiOperation(value = "测试test")
    @GetMapping("/test")
    public Result test(){
        log.info("测试");
        return Result.success("测试","测试结果");
    }
}
