package com.kotei.allweather.service;

import com.kotei.allweather.mapper.DictionaryMapper;
import com.kotei.allweather.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/9
 * 修改历史：
 * 1. [2018/4/9]创建文件
 *
 * @author chunl
 */
@Service("transationalService")
public class TransationalService {


    @Autowired
    private DictionaryMapper mapper;

    @Transactional
    public void testa() {
        mapper.inserta();
        mapper.insertb();
    }
}
