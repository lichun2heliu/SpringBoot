package com.kotei.workflowengine.enums;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/3
 * 修改历史：
 * 1. [2018/4/3]创建文件
 * 流程节点类型
 * @author chunl
 */
public enum WorkflowNodeEnum {

    /**
     * 普通节点
     */
    DEFAULT(0),

    /**
     *  开始节点
     */
    START(1),

    /**
     * 结束节点
     */
    END(2);

    private int value;

    WorkflowNodeEnum(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

}
