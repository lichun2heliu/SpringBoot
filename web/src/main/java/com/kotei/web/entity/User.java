package com.kotei.web.entity;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/7
 * 修改历史：
 * 1. [2018/3/7]创建文件
 *
 * @author chunl
 */
public class User {

    /**
     * 用户ID
     */
    private String id;

    /**
     * 登录账号
     */
    private String account;

    /**
     * 用户姓名
     */
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
