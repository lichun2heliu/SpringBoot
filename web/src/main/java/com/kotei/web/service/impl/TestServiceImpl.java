package com.kotei.web.service.impl;

import com.kotei.web.dao.UserMapper;
import com.kotei.web.entity.User;
import com.kotei.web.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/7
 * 修改历史：
 * 1. [2018/3/7]创建文件
 *
 * @author chunl
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private UserMapper userMapper;

    public List<User> getUsers() {
        return userMapper.getUsers();
    }
}
