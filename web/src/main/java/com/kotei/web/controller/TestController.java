package com.kotei.web.controller;

import com.kotei.web.entity.User;
import com.kotei.web.service.impl.TestServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/2/28
 * 修改历史：
 * 1. [2018/2/28]创建文件
 *
 * @author chunl
 */
@RestController("webTestController")
@RequestMapping("/test")
@Api("测试")
public class TestController {

    @Autowired
    private TestServiceImpl testService;

    @GetMapping(value = "/test")
    @ApiOperation(value = "查询Test分页")
    public String test() {
        return "web test success!";
    }

    @GetMapping(value = "/getUsers")
    @ApiOperation(value = "查询Test分页")
    public List<User> getUsers() {
        //return testService.getUsers();
        return null;
    }
}