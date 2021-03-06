package com.kotei.allweather.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/4
 * 修改历史：
 * 1. [2018/4/4]创建文件
 *
 * @author chunl
 */
@Getter
@Setter
public class TestMongoDBTwo {

    /**
     * MongoDB的Id号
     */
    private int id;

    private String name;

    private String like;

    /**
     * 数字
     */
    private Double digital;
}
