package com.kotei.allweather.controller;

import com.kotei.allweather.entity.TestMongoDBTwo;
import com.kotei.allweather.service.MongoDBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/4
 * 修改历史：
 * 1. [2018/4/4]创建文件
 *
 * @author chunl
 */
@RequestMapping("/mongodb")
@RestController("mongoDBController2")
public class MongoDBController {

    private static final Logger logger = LoggerFactory.getLogger(MongoDBController.class);

    @Autowired
    private MongoDBService<TestMongoDBTwo> service;

    private int id = 2;

    @GetMapping(value = "/inserttwo")
    public boolean insert() {
        TestMongoDBTwo two = new TestMongoDBTwo();
        two.setLike("花花");
        two.setId(id);
        two.setName("名字");
        two.setDigital(12345678913456.123456789);
        service.save(two);
        return true;
    }

    @GetMapping(value = "/testLog")
    public void testLog(){
        logger.trace("logback的--trace日志--输出了");
        logger.debug("logback的--debug日志--输出了");
        logger.info("logback的--info日志--输出了");
        logger.warn("logback的--warn日志--输出了");
        logger.error("logback的--error日志--输出了");
    }



    @GetMapping(value = "/gettwo")
    public TestMongoDBTwo get() {
        return service.get(id, TestMongoDBTwo.class);
    }

}
