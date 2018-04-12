package com.kotei.workflowengine.core;

import com.kotei.workflowengine.enums.WorkflowNodeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/2
 * 修改历史：
 * 1. [2018/4/2]创建文件
 * 工作流节点的基本信息
 * @author chunl
 */
public class NodeInfo {

    /**
     *
     * @param nodeID     节点的ID
     * @param classType  节点对应的类的类型
     * @param nodeType   节点的类型
     */
    public NodeInfo(String nodeID, Class<?> classType, int nodeType) {
        this.NodeID = nodeID;
        this.NodeType = classType;
        if ((nodeType & WorkflowNodeEnum.START.getValue()) == WorkflowNodeEnum.START.getValue())
            this.IsStart = true;
        if ((nodeType & WorkflowNodeEnum.END.getValue()) == WorkflowNodeEnum.END.getValue())
            this.IsEnd = true;
    }

    /**
     * 节点的ID
     */
    @Getter
    @Setter
    public String NodeID;

    /**
     * 节点的类型
     */
    @Getter
    @Setter
    public Class<?> NodeType;

    /**
     * 是否是流程开始节点
     */
    @Getter
    @Setter
    public boolean IsStart;

    /**
     * 是否是流程结束节点
     */
    @Getter
    @Setter
    public boolean IsEnd;

    /**
     * 是否为分支节点
     */
    @Getter
    @Setter
    public boolean IsBranch;

    /**
     * 节点的分支选择器
     */
    @Setter
    @Getter
    public Class<?> BranchSelector;

}
