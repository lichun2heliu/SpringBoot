package com.kotei.core.service;

import com.kotei.core.entity.BaseEntity;

import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/1
 * 修改历史：
 * 1. [2018/3/1]创建文件
 * 统一Service层接口
 * @author chunl
 */
public interface BaseService<T extends BaseEntity> {
    /**
     * 通过id获取单个实体
     * @param id
     * @return
     */
    T get(Long id);

    /**
     * 通过单个主键id删除单个实体
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 通过多个主键id，批量删除实体
     * @param ids
     * @return
     */
    int delete(List<Long> ids);

    /**
     * 保存单个实体对象
     * @param t
     * @return
     */
    int save(T t);

    /**
     * 根据条件查询多个对象
     * @param t
     * @return
     */
    List<T> list(T t);

    /**
     * 根据条件查询统计值
     * @param t
     * @return
     */
    int count(T t);
}
