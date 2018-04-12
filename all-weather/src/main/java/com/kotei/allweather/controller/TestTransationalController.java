package com.kotei.allweather.controller;

import com.kotei.allweather.service.TransationalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/8
 * 修改历史：
 * 1. [2018/4/8]创建文件
 *
 * @author chunl
 */
@RequestMapping("/testTransational")
@RestController("testTransational")
public class TestTransationalController {

    @Autowired
    private TransationalService service;

    @GetMapping("/testa")
    public void testa() {
        service.testa();
    }
}
