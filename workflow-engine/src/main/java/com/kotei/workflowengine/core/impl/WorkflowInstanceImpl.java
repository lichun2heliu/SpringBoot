package com.kotei.workflowengine.core.impl;

import com.kotei.workflowengine.core.*;
import com.kotei.workflowengine.enums.WFInstanceEnum;
import com.kotei.workflowengine.pojo.WFHistory;
import com.kotei.workflowengine.pojo.WFInstance;
import com.kotei.workflowengine.storage.WorkflowStorage;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.Assert.notNull;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/2
 * 修改历史：
 * 1. [2018/4/2]创建文件
 * 流程实例
 * @author chunl
 */
public class WorkflowInstanceImpl implements WorkflowInstance {

    private WorkflowMap map = null;

    private InstanceRuntimeContext context;

    private String current;// 当前节点

    @Autowired
    @Qualifier("wfOracleDBStorage")
    private WorkflowStorage storage;

    @Autowired
    @Qualifier("flowManager")
    private FlowManager flowManager;

    private Logger logger = LoggerFactory.getLogger(WorkflowInstanceImpl.class);


    public WorkflowInstanceImpl(WorkflowMap map) {
        this.map = map;
    }

    /**
     * 使用流程实例数据填充流程实例
     * @param context
     */
    @Override
    public void initialize(InstanceRuntimeContext context) {
        WFInstance obj = context.getInstanceData();
        notNull(obj,"WFInstance can not null");
        if (!obj.getMapID().equals(map.getMapID())){
            throw new IllegalArgumentException("InstanceDataHasError");
        }

        if ((obj.getState() == WFInstanceEnum.CREATED || obj.getState() == WFInstanceEnum.RUNNING)
                && map.containsNode(obj.getNodeID())) {
            // 流程正在进行
            current = obj.getNodeID();
        } else if (obj.getState() == WFInstanceEnum.FINISHED && obj.getNodeID().equals(map.getFinishedNode())) {
            // 流程已经结束
            // 查询流程的最后一个历史节点
            // ReadOnlyCollection<NodeInfo> histories =
            // this.GetHistoryNodeSequences();
            // current = histories[histories.Count - 1].NodeID;
            // 放到Instance外面做
            current = obj.getNodeID();
        }else{
            throw new IllegalArgumentException("InstanceDataHasError");
        }

        this.context = context;
    }

    @Override
    public String getInstanceID() {
        return context.getInstanceData().getInstanceID();
    }

    @Override
    public WorkflowMap getMap() {
        return null;
    }

    @Override
    public String getCurrent() {
        return null;
    }

    @Override
    public List<NodeInfo> getHistoryNodeSequences() {
        List<NodeInfo> list = new ArrayList<NodeInfo>();
        List<WFHistory> histories = storage.getHistories(context.getInstanceData().getInstanceID());
        if (histories.size() > 0) {
            String last = histories.get(histories.size() - 1).getNodeID();
            if (last.equals(map.getFinishedNode())) {
                histories.remove(histories.size() - 1);
            }

            for (WFHistory wfHistory : histories) {
                list.add(map.getNode(wfHistory.getNodeID()));
            }
        }
        return list;
    }

    @Override
    public boolean getFinished() {
        return false;
    }

    @Override
    public NodeDataContext getDataContext() {
        return null;
    }

    @Override
    public String next() {
        return flowManager.next(this);
    }

    @Override
    public String previous() {
        return flowManager.previous(this);
    }

    @Override
    public InstanceRuntimeContext getContext() {
        return null;
    }

    @Override
    public void setContext(InstanceRuntimeContext context) {

    }
}
