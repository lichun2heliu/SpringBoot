package com.kotei.workflowengine.enums;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/30
 * 修改历史：
 * 1. [2018/3/30]创建文件
 *
 * @author chunl
 */
public enum FlowStateEnum {

    DEFAULT(0),BREAK(1),FINISH(2), REDIRECT_NEW(3), REDIRECT_HISTORY(4);

    private int value;

    private FlowStateEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

}
