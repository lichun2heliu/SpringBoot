package com.kotei.allweather.Test;


/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/9
 * 修改历史：
 * 1. [2018/4/9]创建文件
 *
 * @author chunl
 */
public class B implements MyCallInterface  {

    @Override
    public void method() {
        System.out.println("谁来调用我啊。好寂寞！！");
    }

    public static void main(String[] args) {
        Caller caller = new Caller();
        caller.setCallfuc(new B());
        caller.call();
    }

}
