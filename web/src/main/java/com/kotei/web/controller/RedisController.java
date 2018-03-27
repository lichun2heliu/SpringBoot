package com.kotei.web.controller;

import com.kotei.web.entity.People;
import com.kotei.web.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/16
 * 修改历史：
 * 1. [2018/3/16]创建文件
 *
 * @author chunl
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Qualifier("redisService")
    @Autowired
    private RedisService redisService;

    /**
     * id
     */
    public static final String name ="小芳";


    /**
     * add people
     * @return
     */
    @GetMapping("/add")
    public String add(){
        People people = new People("小芳","女","唱歌");
        redisService.addPeople(people.getName(), people);
        return "SCUESS";
    }

    /**
     * get people
     * @return
     */
    @GetMapping("/get")
    public People get(){
        return redisService.getPeople(name);
    }
}
