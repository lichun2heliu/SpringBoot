package com.kotei.workflowengine.core;

import com.kotei.workflowengine.core.impl.WorkflowNodeImpl;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/30
 * 修改历史：
 * 1. [2018/3/30]创建文件
 * 表示流程已经结束的节点
 * @author chunl
 */
public final class FinishedNode extends WorkflowNodeImpl {

    @Override
    protected void previous(NodeFlowContext flowContext)
    {

    }

    @Override
    protected void next(NodeFlowContext flowContext) {

    }
}
