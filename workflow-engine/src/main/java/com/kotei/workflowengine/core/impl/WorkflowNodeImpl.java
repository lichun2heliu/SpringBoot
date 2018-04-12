package com.kotei.workflowengine.core.impl;

import com.kotei.workflowengine.core.NodeFlowContext;
import com.kotei.workflowengine.core.WorkflowNode;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/3
 * 修改历史：
 * 1. [2018/4/3]创建文件
 *
 * @author chunl
 */
public abstract class WorkflowNodeImpl implements WorkflowNode {

    private String nodeID;

    /**
     * @param nodeId the nodeID to set
     */
    @Override
    public void setNodeID(String nodeId) {
        if (nodeId != null && nodeId.equals("")) {
            this.nodeID = nodeId;
        }
    }

    /**
     * @return the nodeID
     */
    @Override
    public String getNodeID() {
        return nodeID;
    }

    @Override
    public void onNext(NodeFlowContext flowContext) {
        this.next(flowContext);
    }

    @Override
    public void onPrevious(NodeFlowContext flowContext) {
        this.previous(flowContext);
    }

    /**
     * 流程退回到上一步时需要处理的业务方法
     *
     * @param flowContext
     */
    protected void previous(NodeFlowContext flowContext) {

    }

    /**
     * 流程流转到下一步时需要处理的业务方法
     *
     * @param flowContext
     */
    protected abstract void next(NodeFlowContext flowContext);
}
