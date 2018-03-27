package com.kotei.core.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/1
 * 修改历史：
 * 1. [2018/3/1]创建文件
 * 统一的分页查询实体
 * @author chunl
 */
@Getter
@Setter
public class PageEntity<T> {

    public enum SortEnum{
        NULL,ASC,DES
    }


    /**
     * 查询第几页
     */
    @ApiModelProperty("查询第几页")
    private Integer pageNo;

    /**
     * 每页显示多少条记录
     */
    @ApiModelProperty("每页显示多少条记录")
    private Integer pageSize;

    /**
     * 查询参数实体
     */
    @ApiModelProperty("查询参数实体")
    private T paramer;

    /**
     * 排序;默认是不需要排序。有升序、降序
     */
    @ApiModelProperty("排序;默认是不需要排序。有升序、降序")
    private SortEnum sort=SortEnum.NULL;

    /**
     * 年份
     */
    @ApiModelProperty("查询年份")
    private Integer year;


}
