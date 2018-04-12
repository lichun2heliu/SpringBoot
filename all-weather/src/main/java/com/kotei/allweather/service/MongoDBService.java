package com.kotei.allweather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/4
 * 修改历史：
 * 1. [2018/4/4]创建文件
 *
 * @author chunl
 */
@Service("mongoDBService2")
public class MongoDBService<T> {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(T t){
        mongoTemplate.save(t);
    }

    public T get(int id, Class<T> entityClass){
        return mongoTemplate.findById(id, entityClass);
    }

}
