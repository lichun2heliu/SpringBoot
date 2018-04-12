package com.kotei.allweather.mapper;

import com.kotei.allweather.entity.DataDictionary;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/4
 * 修改历史：
 * 1. [2018/4/4]创建文件
 *
 * @author chunl
 */
@Repository
public interface DictionaryMapper {

    List<DataDictionary> getAllDictionaries();

    void inserta();

    void insertb();

}
