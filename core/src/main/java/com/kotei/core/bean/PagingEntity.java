package com.kotei.core.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/1
 * 修改历史：
 * 1. [2018/3/1]创建文件
 *
 * @author chunl
 * @param <T>
 */
@Getter
@Setter
public class PagingEntity<T> {

    /**
     * 当前页码
     */
    private int pageNo;


    /**
     * 总页数
     */
    private int pageCount;


    /**
     * 每页记录数
     */
    private int pageSize;


    /**
     * 总记录数
     */
    private int recordCount;


    /**
     * 当前页的数据
     */
    private List<T> currentList;
}
