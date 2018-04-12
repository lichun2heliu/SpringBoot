package com.kotei.workflowengine.core;

import com.kotei.workflowengine.enums.FlowStateEnum;
import com.kotei.workflowengine.enums.FlowTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/2
 * 修改历史：
 * 1. [2018/4/2]创建文件
 * 流程节点流转上下文
 * @author chunl
 */
public class NodeFlowContext {

    private WorkflowInstance instance;

    NodeFlowContext(WorkflowInstance instance) {
        this.instance = instance;
    }

    /**
     * 当前的流程图ID
     * @return
     */
    public String getMapID() {
        return instance.getMap().getMapID();
    }

    /**
     * 流程实例的当前节点
     * @return
     */
    public String getCurrentNode() {
        return instance.getCurrent();
    }

    /**
     * 流程流转的类型
     */
    @Getter
    @Setter
    public FlowTypeEnum flowType;

    /**
     * 流转状态
     */
    @Getter
    public FlowStateEnum State;

    /**
     * 要重定向的节点
     */
    @Getter
    public String RedirectedNode;

    private void checkState() {
        if (State != FlowStateEnum.DEFAULT) {
            return;
        }
    }

    /**
     * 中断流程执行，阻止流程流转到下一个节点，并使流程实例的流转结果返回null
     */
    public void breakFlow() {
        checkState();
        State = FlowStateEnum.BREAK;
    }

    /**
     * 强制流程立即结束
     */
    public void finish() {
        checkState();
        State = FlowStateEnum.FINISH;
    }

    /**
     * 在流程流转到下一步的过程中，将流程重定向到新的节点
     * @param nextNode
     */
    public void redirectNew(String nextNode) {
        if (flowType == FlowTypeEnum.PREVIOUS){
            return;
        }

        checkState();

        if (nextNode == null || nextNode.equals("")){
            return;
        }

        if (!instance.getMap().containsNode(nextNode)){
            return;
        }

        RedirectedNode = nextNode;
        State = FlowStateEnum.REDIRECT_NEW;
    }

    public void redirectHistory(String historyNode) {
        if (flowType == FlowTypeEnum.NEXT){
            return;
        }

        checkState();
        if (historyNode == null || historyNode.equals("")){
            return;
        }

        if (!instance.getMap().containsNode(historyNode)){
            return;
        }

        RedirectedNode = historyNode;
        State = FlowStateEnum.REDIRECT_HISTORY;
    }
}
