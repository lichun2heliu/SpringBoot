package com.kotei.workflowengine.core;

import com.kotei.workflowengine.pojo.WFInstance;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/2
 * 修改历史：
 * 1. [2018/4/2]创建文件
 * 工作流节点数据上下文，用于在流程节点流转过程中传递数据
 * @author chunl
 */
@Getter
@Setter
public class NodeDataContext {

    /**
     * 上一个节点传递的FlowData
     */
    private String fromNodeData;

    /**
     * 本次节点传递给下一个节点的FlowData
     */
    private String toNodeData;

    private WFInstance instanceData;

    public NodeDataContext(WFInstance instanceData) {
        this.instanceData = instanceData;
        this.ServerNamedDatas = new ArrayList<Object>();
    }

    /**
     * 流程实例ID
     */
    public String InstanceID;

    /**
     * 获取或设置在本次流转中，服务端传递给流程节点的数据
     */
    public Object ServerData;

    /**
     * 获取或设置在本次流转中，服务端传递给流程节点的多个命名的数据
     */
    public List<Object> ServerNamedDatas;

    public void SetInstanceData(String name, Object data) {

    }
}
