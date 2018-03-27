package com.kotei.web.dao;

import com.kotei.web.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/7
 * 修改历史：
 * 1. [2018/3/7]创建文件
 *
 * @author chunl
 */
@Mapper
public interface UserMapper {


    /**
     * 查询所有用户
     * @return
     */
    @Select("SELECT * FROM USER WHERE 1=1")
    List<User> getUsers();

}
