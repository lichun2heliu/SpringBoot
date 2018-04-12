package com.kotei.workflowengine.core;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/30
 * 修改历史：
 * 1. [2018/3/30]创建文件
 *
 * @author chunl
 */
@Component
public interface WorkflowInstance {

    /**
     * 使用流程实例数据初始化流程实例对象
     *
     * @param context
     */
    void initialize(InstanceRuntimeContext context);

    /**
     * 流程实例ID
     *
     * @return
     */
    String getInstanceID();

    /**
     * 该流程实例所属的流程图
     *
     * @return
     */
    WorkflowMap getMap();

    /**
     * 该流程实例当前所处的节点
     *
     * @return
     */
    String getCurrent();

    /**
     * 获取当前流程实例已经经过的节点顺序步骤（不包含流程结束的标记节点FinishedNode）
     *
     * @return
     */
    List<NodeInfo> getHistoryNodeSequences();

    /**
     * 流程实例是否已经结束
     *
     * @return
     */
    boolean getFinished();

    /**
     * 获取与当前流程实例相关的数据上下文，包含了要传递给流程节点的数据。
     *
     * @return
     */
    NodeDataContext getDataContext();


    /**
     * 流程流转到下一步
     *
     * @return 如果成功流转到下一步，则返回流转到的下一步的节点ID；否则为null
     */
    String next();

    /**
     * 流程退回到上一步
     * @return 如果成功退回到上一步，则返回退回到的上一步的节点ID；否则为null
     */
    String previous();

    InstanceRuntimeContext getContext();

    void setContext(InstanceRuntimeContext context);
}
