package com.kotei.workflowengine.storage;

import com.kotei.workflowengine.pojo.WFHistory;
import com.kotei.workflowengine.pojo.WFInstance;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/3
 * 修改历史：
 * 1. [2018/4/3]创建文件
 * 工作流数据存储程序
 * @author chunl
 */
@Component
public interface WorkflowStorage {

    /**
     * 创建流程数据
     * @param instance
     * @param history
     */
    void create(WFInstance instance, WFHistory history);

    /**
     * 更新保存流程数据
     * @param instance
     * @param history
     */
    void save(WFInstance instance, WFHistory history);

    WFInstance getInstance(String instanceID);

    /**
     * 已经排序
     * @param instanceID
     * @return
     */
    List<WFHistory> getHistories(String instanceID);

    void delete(String instanceID);

    /**
     * 保存流程信息，并删除最后一条流转历史，用于退回上一步时不保留节点流转过程
     * @param instance
     * @param lastHistory
     */
    void saveAndRemoveLastHistory(WFInstance instance, WFHistory lastHistory);
}
