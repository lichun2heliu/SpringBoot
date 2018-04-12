package com.kotei.workflowengine.core.impl;

import com.kotei.core.utils.collection.Linq;
import com.kotei.workflowengine.core.FlowControl;
import com.kotei.workflowengine.core.NodeFlowContext;
import com.kotei.workflowengine.core.WorkflowInstance;
import com.kotei.workflowengine.enums.FlowStateEnum;
import com.kotei.workflowengine.pojo.WFHistory;
import com.kotei.workflowengine.pojo.WFInstance;
import com.kotei.workflowengine.storage.WorkflowStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/2
 * 修改历史：
 * 1. [2018/4/2]创建文件
 *
 * 不包含节点流转过程记录的流转控制器
 *
 * @author chunl
 */
public class NoTraceFlowControl implements FlowControl {

    @Autowired
    @Qualifier("wfOracleDBStorage")
    private WorkflowStorage storage;


    @Override
    public String previous(WorkflowInstance instance, NodeFlowContext flowContext) {

        if (flowContext.State == FlowStateEnum.REDIRECT_HISTORY) {
            // 重定向新节点
            throw new NotImplementedException();
        }

        List<WFHistory> histories = storage.getHistories(instance.getInstanceID());
        int lastSequence = histories.get(0).getSequence();
        for (WFHistory wfHistory : histories) {
            if (wfHistory.getSequence() > lastSequence) {
                lastSequence = wfHistory.getSequence();
            }
        }

        WFHistory preHistory = Linq.extEquals(histories, "Sequence", lastSequence);
        WFInstance obj = instance.getContext().getInstanceData();
        obj.sequencePtr--;
        WFHistory currentHistory = Linq.extEquals(histories, "Sequence", lastSequence - 1);

        if (currentHistory == null){
            return null;
        }

        obj.setNodeID(currentHistory.getNodeID());
        // 保存数据并删除最后一条历史
        storage.saveAndRemoveLastHistory(obj, preHistory);

        return obj.getNodeID();
    }


}
