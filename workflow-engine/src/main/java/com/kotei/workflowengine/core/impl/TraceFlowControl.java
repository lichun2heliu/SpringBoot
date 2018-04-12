package com.kotei.workflowengine.core.impl;

import com.kotei.core.utils.collection.Linq;
import com.kotei.workflowengine.core.FlowControl;
import com.kotei.workflowengine.core.NodeFlowContext;
import com.kotei.workflowengine.core.WorkflowInstance;
import com.kotei.workflowengine.pojo.WFHistory;
import com.kotei.workflowengine.pojo.WFInstance;
import com.kotei.workflowengine.storage.WorkflowStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.UUID;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/2
 * 修改历史：
 * 1. [2018/4/2]创建文件
 *
 * @author chunl
 */
public class TraceFlowControl implements FlowControl {

    @Autowired
    @Qualifier("wfOracleDBStorage")
    private WorkflowStorage storage;

    @Override
    public String previous(WorkflowInstance instance, NodeFlowContext flowContext) {
        // 如果当前节点已经为流转的第一个节点，则throw
        // 退回上一步 需要保留分支的信息
        // 如果还在分支内，则分支Index--
        // 如果退出了分支的第一个节点，则清除分支信息，认为离开了分支

        // 要重定向的历史节点距离当前节点的步骤数

        List<WFHistory> histories = storage.getHistories(instance.getInstanceID());
        int lastSequence = histories.get(0).getSequence();
        for (WFHistory wfHistory : histories) {
            if (wfHistory.getSequence() > lastSequence) {
                lastSequence = wfHistory.getSequence();
            }
        }

        WFInstance obj = instance.getContext().getInstanceData();
        obj.setSequencePtr(obj.getSequencePtr()+1);

        WFHistory currentHistory = Linq.extEquals(histories, "Sequence", lastSequence - 1);

        obj.setNodeID(currentHistory.getNodeID());

        // 本次流转历史
        WFHistory newHistory = new WFHistory();
        newHistory.setId(UUID.randomUUID().toString());
        newHistory.setInstanceID(obj.getInstanceID());
        newHistory.setNodeID(obj.getNodeID());
        newHistory.setSequence(lastSequence + 1);

        // 保存数据并删除最后一条历史
        storage.save(obj, newHistory);
        return obj.getNodeID();

        //		if (flowContext.State == FlowState.RedirectHistory) {
//			// 重定向新节点
//			List<NodeInfo> historyNodes = instance.getHistoryNodeSequences();
//
//			if (!Linq.extExists(historyNodes, "NodeID", flowContext.RedirectedNode)) {
//				// throw new CannotFlowException("流转历史中没有该节点。");
//				throw new Exception("流转历史中没有该节点。");
//			}
//			// 如果某个节点在历史记录中出现多次，则只找到距离当前节点最近的一次作为重定向的历史节点
//			// 从当前节点的前一步开始循环
//			for (int i = historyNodes.size() - 2, j = 1; i >= 0; i--, j++) {
//				// j：该节点距离当前节点的步骤数
//				if (historyNodes.get(i).getNodeID().equals(flowContext.RedirectedNode)) {
//					preStep = j;
//					break;
//				}
//			}
//			NodeInfo redirectedNode = instance.getMap().getNode(flowContext.RedirectedNode);
//		}
//
//		WFInstance obj = instance.getContext().getInstanceData();
//		// int lastSequence = getLastSequence(instance);
//		int currentSequence = lastSequence + 1;
//		// 获取本次要退回的节点
//		if (flowContext.State == FlowState.RedirectHistory) {
//			// 重定向
//			obj.setSequencePtr(obj.getSequencePtr() - preStep);
//			obj.setNodeID(flowContext.RedirectedNode);
//		} else {
//			// 没有重定向节点
//			obj.setSequencePtr(obj.getSequencePtr() - 1);
//
//
//
//			//WFHistory preHistory = getHistory(instance, obj.getSequencePtr());
//			obj.setNodeID(preHistory.getNodeID());
//		}
//
//		// 本次流转历史
//		WFHistory newHistory = new WFHistory();
//		newHistory.setID(UUID.randomUUID().toString());
//		newHistory.setInstanceID(obj.getInstanceID());
//		newHistory.setNodeID(obj.getNodeID());
//		newHistory.setSequence(currentSequence);
//
//		// 保存数据
//		IWorkflowStorage storage = instance.getContext().getStorage();
//		storage.Save(obj, newHistory);
//		return obj.getNodeID();
    }
}
