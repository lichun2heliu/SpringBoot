package com.kotei.allweather.Test;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/9
 * 修改历史：
 * 1. [2018/4/9]创建文件
 *
 * @author chunl
 */
public class Caller {

    public MyCallInterface mc;

    public void setCallfuc(MyCallInterface mc){
        this.mc = mc;
    }

    public void call(){
        this.mc.method();
    }
}
