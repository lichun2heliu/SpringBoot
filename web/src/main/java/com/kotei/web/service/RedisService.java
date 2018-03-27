package com.kotei.web.service;

import com.kotei.web.entity.People;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/16
 * 修改历史：
 * 1. [2018/3/16]创建文件
 *
 * @author chunl
 */
@Service("redisService")
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * add method
     * @param key
     * @param people
     */
    public void addPeople(String key, People people){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, people);
    }

    /**
     * get method
     * @param key
     * @return
     */
    public People getPeople(String key){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return (People) valueOperations.get(key);
    }
}
