package com.kotei.workflowengine.core;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/30
 * 修改历史：
 * 1. [2018/3/30]创建文件
 * 流程节点
 * @author chunl
 */
public interface WorkflowNode {

    /**
     * 流程节点在某个流程图中的唯一ID
     * @param nodeId
     */
    void setNodeID(String nodeId);

    String getNodeID();

    /**
     * 通知流程节点，流程即将流转到下一步
     * @param flowContext
     */
    void onNext(NodeFlowContext flowContext);

    /**
     * 通知流程节点，流程即将退回到上一步
     * @param flowContext
     */
    void onPrevious(NodeFlowContext flowContext);
}
