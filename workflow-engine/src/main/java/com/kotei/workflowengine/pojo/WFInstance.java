package com.kotei.workflowengine.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kotei.workflowengine.enums.WFInstanceEnum;
import com.kotei.workflowengine.enums.WorkflowNodeEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/3
 * 修改历史：
 * 1. [2018/4/3]创建文件
 *
 * @author chunl
 */
@SuppressWarnings("unused")
public class WFInstance implements Serializable {

    /**
     * 流程实例ID
     */
    @Getter
    @Setter
    public String instanceID;

    /**
     * 流程图ID
     */
    @Getter
    @Setter
    public String mapID;

    /**
     * 当前节点ID
     */
    @Getter
    @Setter
    public String nodeID;

    /**
     * 流程实例状态
     */
    @Getter
    @Setter
    public WFInstanceEnum state;

    /**
     * 当前节点所处的流程步骤指针
     */
    @Getter
    @Setter
    public int sequencePtr;

    /**
     * 流程实例对所有节点的共享数据
     */
    @Getter
    @Setter
    public String instanceData;

    /**
     * 流程流转时传递给下一个节点的数据
     */
    @Getter
    @Setter
    public String flowData;

    /**
     * 流程流转的分支路线信息
     */
    @Getter
    @Setter
    public String route;

    /**
     * 用于数据库操作字段，没任何意义。
     */
    @JsonIgnore
    @Setter
    @Getter
    public int count;

    @Setter
    @Getter
    public Integer version;
}
