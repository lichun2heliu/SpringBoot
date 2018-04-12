package com.kotei.workflowengine.core;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/30
 * 修改历史：
 * 1. [2018/3/30]创建文件
 * 流转控制器
 * @author chunl
 */
public interface FlowControl {

    /**
     * 上一步
     * @param instance
     * @param flowContext
     * @return
     */
    String previous(WorkflowInstance instance, NodeFlowContext flowContext);
}
