package com.kotei.workflowengine.enums;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/30
 * 修改历史：
 * 1. [2018/3/30]创建文件
 *
 * @author chunl
 */
public enum FlowTypeEnum {

    NEXT(0),PREVIOUS(1);

    private int value;

    FlowTypeEnum(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
