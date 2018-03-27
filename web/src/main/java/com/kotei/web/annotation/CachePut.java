package com.kotei.web.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/19
 * 修改历史：
 * 1. [2018/3/19]创建文件
 *
 * @author chunl
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface CachePut {

    @AliasFor("cacheNames")
    String[] value() default{};

}
