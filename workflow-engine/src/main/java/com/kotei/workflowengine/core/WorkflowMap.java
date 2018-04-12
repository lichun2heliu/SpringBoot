package com.kotei.workflowengine.core;

import com.kotei.workflowengine.pojo.WFInstance;

import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/30
 * 修改历史：
 * 1. [2018/3/30]创建文件
 * 流程图。表示流程的架构定义、流程节点之间的关系。
 * @author chunl
 */
public interface WorkflowMap {

    /**
     * 流程图的ID
     * @return
     */
    String getMapID();

    /**
     * 添加流程节点
     * @param nodeID   流程节点对应的类
     * @param nodeType 节点ID
     * @param <TNode>  流程节点类型
     */
    <TNode extends WorkflowNode> void addNode(String nodeID, int nodeType);

    /**
     * 添加流程节点
     * @param nodeID    节点ID
     * @param classType 流程节点对应的类的类型
     * @param nodeType  流程节点类型
     */
    void addNode(String nodeID, Class<?> classType, int nodeType);

    /**
     * 设置节点的下一步节点
     * @param nodeID  节点ID
     * @param next    该节点的下一步节点的ID
     */
    void setNext(String nodeID, String next);

    /**
     * 给节点添加分支
     * @param nodeID
     * @param selectorType
     * @param branchName
     * @param nextNodes
     */
    void addBranch(String nodeID, Class<?> selectorType, String branchName, String... nextNodes);

    /**
     * 获取某个流程节点的信息
     * @param nodeID 节点的ID
     * @return
     */
    NodeInfo getNode(String nodeID);

    /**
     * 获取流程图中定义的所有流程节点
     * @return
     */
    List<NodeInfo> getNodes();

    /**
     * 获取某个流程节点的下一步节点
     * @param nodeID
     * @return
     */
    NodeInfo getNextNode(String nodeID);

    /**
     * 判断某个节点是否包含某个分支
     * @param nodeID
     * @param branchName
     * @return
     */
    boolean hasBranch(String nodeID, String branchName);

    /**
     * 获取分支定义的第一个节点
     * @param nodeID
     * @param branchName
     * @return
     */
    String getBranchFirstNode(String nodeID, String branchName);

    /**
     * 判断流程图中是否包含指定ID的节点
     * @param nodeID
     * @return
     */
    boolean containsNode(String nodeID);

    /**
     * 设置流程的完结节点（表示流程已经结束的标记节点）
     * @return
     */
    String getFinishedNode();

    void setFinishedNode(String finishedNode);

    /**
     * 创建一个新的流程实例
     * @param environment
     * @return
     */
    WorkflowInstance createInstance(RuntimeEnvironment environment);

    /**
     * 通过流程实例数据，获取一个已经存在的流程实例
     * @param instanceData 流程实例数据
     * @param environment
     * @return
     */
    WorkflowInstance getInstance(WFInstance instanceData, RuntimeEnvironment environment);

    /**
     * 获取某个节点分支的完整路径
     * @param nodeID
     * @param branchName
     * @return
     */
    List<String> getBranchNodeSequences(String nodeID, String branchName);

    /**
     * 获取流程图中定义的开始节点
     * @return
     */
    NodeInfo getStartNode();

    /**
     * 获取流程图中定义的所有结束节点
     * @return
     */
    List<NodeInfo> getEndNodes();

    boolean getKeep();
}
