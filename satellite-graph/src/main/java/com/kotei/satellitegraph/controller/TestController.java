package com.kotei.satellitegraph.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/2/28
 * 修改历史：
 * 1. [2018/2/28]创建文件
 *
 * @author chunl
 */
@RestController("satelliteGraphTestController")
@RequestMapping("/satellitegraph")
public class TestController {

    @GetMapping(value = "/test")
    public String test(){
        return "all weather test success!";
    }

}
