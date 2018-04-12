package com.kotei.allweather.controller;

import com.kotei.allweather.entity.DataDictionary;
import com.kotei.allweather.service.MybatisService;
import com.kotei.core.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/4
 * 修改历史：
 * 1. [2018/4/4]创建文件
 *
 * @author chunl
 */
@RequestMapping("/mybatis")
@RestController("mybatis2")
public class MybatisController {

    @Autowired
    private MybatisService service;

    @GetMapping("/getAllDictionaries")
    public List<DataDictionary> getAllDictionaries(){
        return service.getAllDictionaries();
    }

    @GetMapping("/testException")
    public void testException(){
        throw new BaseException("测试全局异常");
    }

}
