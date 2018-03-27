package com.kotei.web.entity;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.*;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/16
 * 修改历史：
 * 1. [2018/3/16]创建文件
 *
 * @author chunl
 */
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class People {

    /**
     * name
     */
    private String name;

    /**
     * gender
     */
    private String gender;

    /**
     * like
     */
    private String like;
}
