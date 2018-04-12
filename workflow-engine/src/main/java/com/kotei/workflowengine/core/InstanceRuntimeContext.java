package com.kotei.workflowengine.core;

import com.kotei.workflowengine.pojo.WFInstance;
import com.kotei.workflowengine.storage.WorkflowStorage;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/30
 * 修改历史：
 * 1. [2018/3/30]创建文件
 *
 * @author chunl
 */
public final class InstanceRuntimeContext {

    /**
     * 流程数据的存储程序
     */
    private WorkflowStorage storage;

    /**
     * 流程实例的数据信息
     */
    private WFInstance instanceData;

    public WorkflowStorage getStorage() {
        return storage;
    }

    public void setStorage(WorkflowStorage storage) {
        this.storage = storage;
    }

    public WFInstance getInstanceData() {
        return instanceData;
    }

    public void setInstanceData(WFInstance instanceData) {
        this.instanceData = instanceData;
    }
}
