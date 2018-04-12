package com.kotei.allweather.service;

import com.kotei.allweather.mapper.DictionaryMapper;
import com.kotei.allweather.entity.DataDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/4
 * 修改历史：
 * 1. [2018/4/4]创建文件
 *
 * @author chunl
 */
@Service("mybatisServicetwo")
public class MybatisService {

    @Autowired
    private DictionaryMapper mapper;

    public List<DataDictionary> getAllDictionaries() {
        return mapper.getAllDictionaries();
    }
}
