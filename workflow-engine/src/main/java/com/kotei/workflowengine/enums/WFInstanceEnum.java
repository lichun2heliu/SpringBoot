package com.kotei.workflowengine.enums;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/3
 * 修改历史：
 * 1. [2018/4/3]创建文件
 * 流程实例状态
 * @author chunl
 */
public enum WFInstanceEnum {

    /**
     * 流程已经发起，已经进入开始节点（流程实例数据已经创建）
     */
    CREATED(1),

    /**
     * 流程正在进行
     */
    RUNNING(2),

    /**
     * 流程已经完成结束
     */
    FINISHED(3);

    private int value;

    WFInstanceEnum(int value) {
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}
