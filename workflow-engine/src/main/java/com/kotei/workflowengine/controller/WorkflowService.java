package com.kotei.workflowengine.controller;

import com.kotei.workflowengine.pojo.WFObject;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/30
 * 修改历史：
 * 1. [2018/3/30]创建文件
 *
 * @author chunl
 */
public interface WorkflowService {

    /**
     * 发起一个新的流程
     * @param mapID 要发起的流程的流程图ID
     * @return 如果一个流程发起成功，则返回流程实例的ID
     */
    WFObject start(String mapID);

    /**
     * 流程实例流转到下一步
     * @param obj 要流转的流程实例
     * @return 如果成功流转到下一步，则返回流转到的下一步的节点ID
     */
    WFObject next(@RequestBody WFObject obj);

    /**
     * 获取流程实例已经经过的节点顺序步骤
     * @param instanceID
     * @return
     */
    String[] getHistoryNodeSequences(String instanceID);

    /**
     * 获取流程实例当前所处的节点
     * @param instanceID
     * @return
     */
    WFObject getCurrent(String instanceID);

    /**
     * 获取流程图定义的开始节点
     * @param mapID
     * @return
     */
    String getStartNode(String mapID);

    /**
     * 获取流程图定义的所有结束节点
     * @param mapID
     * @return
     */
    String[] getEndNodes(String mapID);
}
