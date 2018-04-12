package com.kotei.workflowengine.core;

import com.kotei.core.utils.collection.Linq;
import com.kotei.workflowengine.core.impl.NoTraceFlowControl;
import com.kotei.workflowengine.core.impl.TraceFlowControl;
import com.kotei.workflowengine.enums.FlowStateEnum;
import com.kotei.workflowengine.enums.FlowTypeEnum;
import com.kotei.workflowengine.enums.WFInstanceEnum;
import com.kotei.workflowengine.exception.CannotFlowException;
import com.kotei.workflowengine.pojo.WFBranchRoute;
import com.kotei.workflowengine.pojo.WFHistory;
import com.kotei.workflowengine.pojo.WFInstance;
import com.kotei.workflowengine.storage.WorkflowStorage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/30
 * 修改历史：
 * 1. [2018/3/30]创建文件
 * 流程流转控制管理器，负责管理流程图的所有流程实例的流转。
 * @author chunl
 */
@Component("flowManager")
public class FlowManager {

    @Autowired
    @Qualifier("wfOracleDBStorage")
    private WorkflowStorage storage;

    private static Logger logger = Logger.getLogger(FlowManager.class);


    /**
     * 发起一个流程
     *
     * @param map
     * @param context
     * @return
     */
    public WFInstance start(WorkflowMap map, InstanceRuntimeContext context) {
        // 创建一个新的流程实例数据
        NodeInfo startNode = map.getStartNode();
        WFInstance obj = new WFInstance();
        obj.setInstanceID(UUID.randomUUID().toString());
        obj.setMapID(map.getMapID());
        obj.setNodeID(startNode.getNodeID());
        obj.setState(WFInstanceEnum.CREATED);
        obj.setSequencePtr(0);
        obj.setFlowData(null);
        obj.setInstanceData(null);

        // 流程本次流转历史
        WFHistory history = new WFHistory();
        history.setId(UUID.randomUUID().toString());
        history.setInstanceID(obj.getInstanceID());
        history.setNodeID(obj.getNodeID());
        history.setSequence(0);

        storage.create(obj, history);
        return obj;
    }

    /**
     * 将流程实例流转到下一步
     *
     * @param instance
     * @return 已经流转到的下一个节点ID
     * @throws CannotFlowException
     */
    public String next(WorkflowInstance instance) throws CannotFlowException {
        if (instance.getFinished())
            throw new CannotFlowException("流程已经结束。");

        NodeFlowContext flowContext = new NodeFlowContext(instance);
        flowContext.flowType = FlowTypeEnum.NEXT;
        NodeInfo current = instance.getMap().getNode(instance.getCurrent());
        try {
            flow(instance, current, flowContext);
        } catch (InstantiationException | IllegalAccessException e1) {
            e1.printStackTrace();
            logger.error("", e1);
        }

        NodeInfo next = null; // 下一个节点

        String result = tryDealState(flowContext, instance); // 返回值为"",表示flase
        if (!result.equals(""))
            return result;

        if (flowContext.State == FlowStateEnum.REDIRECT_NEW) {
            // 拦截器重定向新节点
            NodeInfo redirectedNode = instance.getMap().getNode(flowContext.RedirectedNode);
            next = redirectedNode;
            // 判断 如果新节点在当前分支上，则移动分支节点位置索引
            // 如果不在 则清除分支历史
            WFBranchRoute route = WFBranchRoute.from(instance.getContext().getInstanceData().getRoute());
            if (null != route) {
                List<String> list = instance.getMap().getBranchNodeSequences(route.NodeID, route.BranchName);
                // A - B - C - D - A - E - D
                // 情况1：当前在B，重定向到D，那么是第一个D还是第二个D？（第一个D）
                // 情况2：当前在B，重定向到A，是之前的A还是后面的A？（后面的A）
                String[] array = (String[]) list.toArray();
                int index = -1;
                // int index = Array.IndexOf(array, next.NodeID,
                // route.Index);//从当前节点的后面开始找
                if (array.length > route.Index) {
                    for (int i = route.Index; i < array.length; i++) {
                        if (array[i].equals(next.NodeID)) {
                            index = i;
                        }
                    }
                }

                if (-1 == index) {
                    // 新节点不在分支上
                    instance.getContext().getInstanceData().setRoute(null);
                } else {
                    // 新节点在分支上
                    route.Index = index;
                    instance.getContext().getInstanceData().setRoute(route.toString());
                }
            } else {
                // 当前不在分支上
                instance.getContext().getInstanceData().setRoute(null);
            }
        } else {
            // 没有重定向节点
            try {
                next = getNextNode(current, instance);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        if(next == null){
            saveFinishedData(instance);
            return instance.getMap().getFinishedNode();
        }

        if (next.getNodeID() == instance.getMap().getFinishedNode()) {
            saveFinishedData(instance);
            return instance.getMap().getFinishedNode();
        }

        // 更新保存数据

        WFInstance obj = instance.getContext().getInstanceData();
        if (obj.getState() == WFInstanceEnum.CREATED)
            obj.setState(WFInstanceEnum.RUNNING);
        int lastSequence = getLastSequence(instance);
        int currentSequence = lastSequence + 1;
        // 更新流程实例信息
        obj.setNodeID(next.getNodeID());
        obj.setSequencePtr(currentSequence);

        // 清除上一个节点传递来的数据，设置传递给下一个节点的数据
        // instance.getContext().getInstanceData().setFlowData(instance.getDataContext().toNodeData);
        // TODO

        // 更新共享数据

        // 新增本次流转历史
        WFHistory newHistory = new WFHistory();
        newHistory.setId(UUID.randomUUID().toString());
        newHistory.setInstanceID(obj.getInstanceID());
        newHistory.setNodeID(obj.getNodeID());
        newHistory.setSequence(currentSequence);

        storage.save(obj, newHistory);
        return next.getNodeID();
    }

    /**
     * 流程公共状态处理
     *
     * @param flowContext
     * @param instance
     * @return 处理成功返回true，没有处理返回false。这里返回""表示false，其他为true
     */
    private String tryDealState(NodeFlowContext flowContext, WorkflowInstance instance) {
        String result = null;
        switch (flowContext.State) {
            case BREAK:
                result = null;
                return result;
            case FINISH:
                saveFinishedData(instance);
                result = instance.getMap().getFinishedNode();
                return result;
            default:
                result = ""; // 返回false
                return result;
        }
    }

    /**
     * 将流程实例退回到上一步
     *
     * @param instance
     * @return 已经退回到的上一个节点ID
     * @throws Exception
     */
    public String previous(WorkflowInstance instance){
        if (instance.getFinished())
            throw new CannotFlowException("流程已经结束。");

        NodeFlowContext flowContext = new NodeFlowContext(instance);
        flowContext.flowType = FlowTypeEnum.PREVIOUS;

        NodeInfo current = instance.getMap().getNode(instance.getCurrent());

        // if (current.IsStart) //如果流转过程中经过了start节点，则该判断会导致无法继续回退上一步
        // throw new CannotFlowException();
        // start -> a ->start -> b ->c 这种流转历史（下一步、回退、下一步、下一步，如果从c开始一直回退则会有问题）
        // 除非是判断当前节点的步骤是处于流程的第一步时，才无法回退
        try {
            flow(instance, current, flowContext);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }

        String result = tryDealState(flowContext, instance); // 返回""表示false
        if (result != "")
            return result;

        // 以下两段是
        boolean keep = instance.getMap().getKeep();
        if (!keep) {
            NoTraceFlowControl flowControl = new NoTraceFlowControl();
            return flowControl.previous(instance, flowContext);
        } else {
            TraceFlowControl flowControl = new TraceFlowControl();
            return flowControl.previous(instance, flowContext);
        }

        /** 下面代码是满足武汉市违法案件需求 **/
        // 如果当前节点已经为流转的第一个节点，则throw
        // 退回上一步 需要保留分支的信息
        // 如果还在分支内，则分支Index--
        // 如果退出了分支的第一个节点，则清除分支信息，认为离开了分支

        // 要重定向的历史节点距离当前节点的步骤数
        // int preStep = 0;
        //
        // if (flowContext.State == FlowState.RedirectHistory) {
        // // 重定向新节点
        // List<NodeInfo> historyNodes = instance.getHistoryNodeSequences();
        //
        // if (!Linq.extExists(historyNodes, "NodeID",
        // flowContext.RedirectedNode)) {
        // // throw new CannotFlowException("流转历史中没有该节点。");
        // throw new Exception("流转历史中没有该节点。");
        // }
        // // 如果某个节点在历史记录中出现多次，则只找到距离当前节点最近的一次作为重定向的历史节点
        // // 从当前节点的前一步开始循环
        // for (int i = historyNodes.size() - 2, j = 1; i >= 0; i--, j++) {
        // // j：该节点距离当前节点的步骤数
        // if
        // (historyNodes.get(i).getNodeID().equals(flowContext.RedirectedNode))
        // {
        // preStep = j;
        // break;
        // }
        // }
        // NodeInfo redirectedNode =
        // instance.getMap().getNode(flowContext.RedirectedNode);
        // }
        //
        // WFInstance obj = instance.getContext().getInstanceData();
        // int lastSequence = getLastSequence(instance);
        // int currentSequence = lastSequence + 1;
        // // 获取本次要退回的节点
        // if (flowContext.State == FlowState.RedirectHistory) {
        // // 重定向
        // obj.setSequencePtr(obj.getSequencePtr() - preStep);
        // obj.setNodeID(flowContext.RedirectedNode);
        // } else {
        // // 没有重定向节点
        // obj.setSequencePtr(obj.getSequencePtr() - 1);
        // WFHistory preHistory = getHistory(instance, obj.getSequencePtr());
        // obj.setNodeID(preHistory.getNodeID());
        // }
        //
        // //本次流转历史
        // WFHistory newHistory = new WFHistory();
        // newHistory.setID(UUID.randomUUID().toString());
        // newHistory.setInstanceID(obj.getInstanceID());
        // newHistory.setNodeID(obj.getNodeID());
        // newHistory.setSequence(currentSequence);
        //
        // //保存数据
        // IWorkflowStorage storage = instance.getContext().getStorage();
        // storage.Save(obj, newHistory);
        // return obj.getNodeID();
    }

    private static void flow(WorkflowInstance instance, NodeInfo node, NodeFlowContext flowContext)
            throws CannotFlowException, InstantiationException, IllegalAccessException {
        if (instance.getFinished())
            throw new CannotFlowException("WorkflowHasFinished");

        // 创建流程节点实例，并且设置NodeID
        WorkflowNode current = (WorkflowNode) node.NodeType.newInstance();
        current.setNodeID(node.getNodeID());

        // 通知当前节点流程即将流转
        if (flowContext.flowType == FlowTypeEnum.NEXT) {
            current.onNext(flowContext);
        } else if (flowContext.flowType == FlowTypeEnum.PREVIOUS) {
            current.onPrevious(flowContext);
        }
    }

    /**
     * 获取标记流程已经结束节点的NodeInfo
     *
     * @param finishedNode
     * @return
     * @throws ClassNotFoundException
     */
    public static NodeInfo getFinishedNodeInfo(String finishedNode) throws ClassNotFoundException {
        return new NodeInfo(finishedNode, FinishedNode.class, -1);
    }

    /**
     * 获取当前节点的下一个节点
     *
     * @param current
     * @param instance
     * @return
     * @throws Exception
     */
    private static NodeInfo getNextNode(NodeInfo current, WorkflowInstance instance) throws Exception {
        // End节点之后的下一个节点被忽略
        if (current.isIsEnd()) {
            return getFinishedNodeInfo(instance.getMap().getFinishedNode());
        }

        WFBranchRoute route = WFBranchRoute.from(instance.getContext().getInstanceData().getRoute());
        if (null != route) {
            // 当前节点是从分支流转过来的
            // 判断分支是否结束
            List<String> list = instance.getMap().getBranchNodeSequences(route.NodeID, route.BranchName);
            if (route.Index == list.size() - 1) {
                // 当前节点为分支的最后一个节点
                // 分支已经走完了，则按照普通情况继续走
            } else {
                // 分支还没有走完，则继续按照分支来走
                String nextID = list.get(++route.Index);
                NodeInfo next = instance.getMap().getNode(nextID);
                // 更新数据
                instance.getContext().getInstanceData().setRoute(route.toString());
                return next;
            }
        }

        // 不是从分支走来的
        if (current.IsBranch) {
            // 是分支节点
            // 获取分支选择器
            Assert.notNull(current.BranchSelector,"branch selector not null");
            BranchSelector selector = (BranchSelector) current.BranchSelector.newInstance();
            // 如果流转到当前节点时 分支已经走完了 那么就是null
            String branchName = selector.selectBranch(instance.getDataContext(), current.getNodeID(),
                    route == null ? null : route.BranchName);
            // 所选择的branchName是否包含在流程图分支定义中
            // 分支名称是 NodeID:分支name
            // 获取分支的节点
            if (!instance.getMap().hasBranch(current.getNodeID(), branchName))
                // throw new ArgumentOutOfRangeException("没有定义" + branchName +
                // "分支");
                throw new Exception("没有定义" + branchName + "分支");

            String nextBranchNode = instance.getMap().getBranchFirstNode(current.getNodeID(), branchName);
            NodeInfo nextNode = instance.getMap().getNode(nextBranchNode);
            // 保存选择的分支
            WFBranchRoute r = new WFBranchRoute();
            r.NodeID = current.getNodeID();
            r.BranchName = branchName;
            r.Index = 0;

            instance.getContext().getInstanceData().setRoute(r.toString());
            return nextNode;
        } else {
            // 不是分支
            NodeInfo target = instance.getMap().getNextNode(current.getNodeID());
            if (null == target) {
                throw new Exception("没有找到下一步节点");
            }
            return target;
        }
    }

    /**
     * 获取流程的最后一条流转历史序号
     *
     * @param instance
     * @return
     */
    private int getLastSequence(WorkflowInstance instance) {
        List<WFHistory> histories = storage.getHistories(instance.getInstanceID());
        int lastSequence = 0;
        for (WFHistory wfHistory : histories) {
            if (lastSequence < wfHistory.getSequence()) {
                lastSequence = wfHistory.getSequence();
            }
        }
        return lastSequence;
    }

    /**
     * 获取流程的某一条流转历史
     *
     * @param instance
     * @param sequence
     *            流转历史的序号
     * @return
     */
    private WFHistory getHistory(WorkflowInstance instance, int sequence) {
        WFHistory history = (WFHistory) Linq.extEqualsList(storage.getHistories(instance.getInstanceID()),
                new String[] { "InstanceID", "Sequence" }, new Object[] { instance.getInstanceID(), sequence });
        return history;
    }

    /**
     * 保存流程结束信息
     *
     * @param instance
     */
    public void saveFinishedData(WorkflowInstance instance) {
        WFInstance obj = instance.getContext().getInstanceData();
        obj.setNodeID(instance.getMap().getFinishedNode());
        obj.setState(WFInstanceEnum.FINISHED);
        int lastSequence = getLastSequence(instance);
        int currentSequence = lastSequence + 1;
        obj.setSequencePtr(currentSequence);

        WFHistory history = new WFHistory();
        history.setId(UUID.randomUUID().toString());
        history.setInstanceID(obj.getInstanceID());
        history.setNodeID(obj.getNodeID());
        history.setSequence(currentSequence);

        storage.save(obj, history);
    }

}
