package com.kotei.workflowengine.pojo;

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
public class WFHistory implements Serializable {

    /**
     * 数据标识ID
     */
    @Getter
    @Setter
    public String id;

    /**
     * 流程实例ID
     */
    @Getter
    @Setter
    public String instanceID;

    /**
     * 本次流转的节点
     */
    @Getter
    @Setter
    public String nodeID;

    /**
     * 流程节点流转顺序
     */
    @Getter
    @Setter
    public int sequence;

}
