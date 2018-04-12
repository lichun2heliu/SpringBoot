package com.kotei.workflowengine.core.impl;

import com.kotei.core.utils.collection.Linq;
import com.kotei.workflowengine.core.*;
import com.kotei.workflowengine.exception.CannotFlowException;
import com.kotei.workflowengine.pojo.WFInstance;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/2
 * 修改历史：
 * 1. [2018/4/2]创建文件
 * 流程图
 * @author chunl
 */
public class WorkflowMapImpl implements WorkflowMap {

    private String mapID;

    private boolean keep;

    /**
     * 流程图定义
     */
    private NetGraph graph;

    /**
     * 节点-分支列表
     */
    private Map<String, SortedSet<String>> branches;

    private String finishedNode;

    private FlowManager flowManager;

    /**
     * 默认的流程已经结束的标记节点ID
     */
    private final String DefaultFinishedNodeID = "Workflow_FinishedNode";

    public WorkflowMapImpl(String mapID, boolean keep) {
        Assert.hasText(mapID,"mapID 不能为空");
        this.mapID = mapID;
        this.keep = keep;
        graph = new NetGraph();
        branches = new HashMap<String, SortedSet<String>>();
    }

    @Override
    public String getMapID() {
        return null;
    }

    /**
     * 添加流程节点
     * @param nodeID   流程节点对应的类
     * @param nodeType 节点ID
     * @param <TNode> 流程节点类型
     */
    @Override
    public <TNode extends WorkflowNode> void addNode(String nodeID, int nodeType) {
        Assert.hasText(nodeID,"nodeID not null");
        addNodeCore(nodeID, WorkflowNode.class, nodeType);
    }

    /**
     * 添加流程节点
     * @param nodeID    节点ID
     * @param classType 流程节点对应的类的类型
     * @param nodeType  流程节点类型
     */
    @Override
    public void addNode(String nodeID, Class<?> classType, int nodeType) {
        Assert.hasText(nodeID,"nodeID not null");
        Assert.notNull(classType,"classType not null");
        Assert.isTrue(!classType.isPrimitive(),"class is primitive type");
        Assert.isTrue(WorkflowNode.class.isAssignableFrom(classType),"class must implement workflowNode");
        addNodeCore(nodeID, classType, nodeType);
    }

    private void addNodeCore(String nodeID, Class<?> classType, int nodeType) {
        NodeInfo node = new NodeInfo(nodeID, classType, nodeType);
        if (nodeID.equals(this.finishedNode)){
            throw new IllegalArgumentException();
        }

        graph.addNode(nodeID, node);
    }

    /**
     * 设置某个节点的下一步节点
     * @param nodeID  节点ID
     * @param next    该节点的下一步节点的ID
     */
    @Override
    public void setNext(String nodeID, String next) {
        Assert.hasText(nodeID,"nodeID not null");
        graph.setOutArcs(nodeID, next);
    }

    /**
     *
     * @param nodeID
     * @param selectorType
     * @param branchName
     * @param nextNodes
     */
    @Override
    public void addBranch(String nodeID, Class<?> selectorType, String branchName, String... nextNodes) {
        Assert.isTrue(BranchSelector.class.isAssignableFrom(selectorType),"selectorType must implement BranchSelector");

        NodeInfo node = graph.getNode(nodeID);
        node.IsBranch = true;
        node.BranchSelector = selectorType;
        // 按照顺序，依次将分支添加到节点
        String current = nodeID;
        for (int i = 0; i < nextNodes.length; i++) {
            graph.addOutArcs(current, nextNodes[i]);
            // 给边添加权值
            Set<String> sets = graph.getArc(current, nextNodes[i]);
            // 分支内所有节点的权值应以分支节点名+分支名 全部一致
            String newName = getBranchName(nodeID, branchName);

            sets.add(newName);
            current = nextNodes[i];

            // node的出边添加了权值，意味着next的这条入边也应该加权值
        }

        // 添加分支 到分支列表
        SortedSet<String> list = branches.get(nodeID);
        if (list == null) {
            list = new TreeSet<String>();
            branches.put(nodeID, list);
        }
        list.add(branchName);
    }

    /**
     *
     * @param nodeID 节点的ID
     * @return
     */
    @Override
    public NodeInfo getNode(String nodeID) {
        Assert.hasText(nodeID, "nodeID not null");
        return graph.getNode(nodeID);
    }

    /**
     *
     * @return
     */
    @Override
    public List<NodeInfo> getNodes() {
        List<NodeInfo> nodeInfos = new ArrayList<NodeInfo>();
        List<NetGraph.NGraphNode> list = graph.getAllNodes();
        for (NetGraph.NGraphNode nGraphNode : list) {
            nodeInfos.add(nGraphNode.getValue());
        }
        return Collections.unmodifiableList(nodeInfos);
    }

    /**
     * 获取该节点的后继节点（下一步节点）
     * @param nodeID
     * @return
     */
    @Override
    public NodeInfo getNextNode(String nodeID) {
        Assert.hasText(nodeID,"nodeID not null");
        List<NetGraph.NGraphNode> nextNodes = graph.getOutNodes(nodeID);
        // 下一步可能有很多命名的分支节点流经，只需要获取唯一的一个命名为null的节点就是定义的nextNode
        for (NetGraph.NGraphNode node : nextNodes) {
            Set<String> sets = graph.getArc(nodeID, node.getKey());
            if (sets == null || sets.size() == 0){
                return node.getValue();
            }
        }

        // 没有找到下一步节点
        return null;
    }

    /**
     *
     * @param nodeID
     * @param branchName
     * @return
     */
    @Override
    public boolean hasBranch(String nodeID, String branchName) {
        SortedSet<String> sets = branches.get(nodeID);
        if (sets != null) {
            return sets.contains(branchName);
        }
        return false;
    }

    @Override
    public String getBranchFirstNode(String nodeID, String branchName) {
        // 获取节点的所有出弧，然后获取包含指定branchName权值的弧
        String key = nodeID + ":" + branchName;
        List<String> arcs = graph.getOutArcs(nodeID);
        for (String arc : arcs) {
            Set<String> sets = graph.getArc(nodeID, arc);
            if (sets.contains(key)) {
                return arc;
            }
        }
        return null;
    }

    private WorkflowInstance CreateInstanceObject() {
        return (WorkflowInstance) new WorkflowInstanceImpl(this);
    }

    /**
     * 判断流程图中是否包含指定ID的节点
     * @param nodeID
     * @return
     */
    @Override
    public boolean containsNode(String nodeID) {
        if (nodeID == null || nodeID.equals("")) {
            return false;
        }
        return graph.Contains(nodeID);
    }

    /**
     *
     * @return the finishedNode
     */
    @Override
    public String getFinishedNode() {
        if (finishedNode == null){
            return DefaultFinishedNodeID;
        }

        return finishedNode;
    }

    private String getBranchName(String nodeID, String branchName) {
        return nodeID + ":" + branchName;
    }

    /**
     * the finishedNode to set
     * @param finishedNode
     */
    @Override
    public void setFinishedNode(String finishedNode) {
        Assert.hasText(finishedNode,"finishedNode not null");
        if (graph.Contains(finishedNode)) {
            throw new IllegalArgumentException("MapContainsNodeID");
        }else{
            this.finishedNode = finishedNode;
        }
    }

    /**
     *
     * @param environment
     * @return
     */
    @Override
    public WorkflowInstance createInstance(RuntimeEnvironment environment) {
        // 创建流程实例对象
        InstanceRuntimeContext context = environment.createContext();

        WorkflowInstance instance = this.CreateInstanceObject();
        instance.setContext(context);

        // 创建流程实例数据
        WFInstance instanceData = flowManager.start(this, context);
        if (instanceData == null){
            return null;
        }

        context.setInstanceData(instanceData);
        instance.initialize(context);
        return instance;
    }

    /**
     *
     * @param instanceData 流程实例数据
     * @param environment
     * @return
     */
    @Override
    public WorkflowInstance getInstance(WFInstance instanceData, RuntimeEnvironment environment) {
        Assert.notNull(instanceData,"instanceData not null");
        InstanceRuntimeContext context = environment.createContext();

        context.setInstanceData(instanceData);
        WorkflowInstance instance = this.CreateInstanceObject();
        instance.initialize(context);

        instance.setContext(context);
        return instance;
    }

    /**
     *
     * @param nodeID
     * @param branchName
     * @return
     */
    @Override
    public List<String> getBranchNodeSequences(String nodeID, String branchName) {
        List<String> list = new ArrayList<String>();
        String key = nodeID + ":" + branchName;
        List<String> arcs = graph.getOutArcs(nodeID);
        // 该节点的出弧是否包含该分支
        boolean hasBranch;
        do {
            // 先假定该节点的出弧不包含分支
            hasBranch = false;
            for (String s : arcs) {
                // 获取弧的权值
                Set<String> sets = graph.getArc(nodeID, s);
                if (sets.contains(key)) {
                    // 该弧的权值中包含该值，则说明该弧包含该分支
                    list.add(s);
                    hasBranch = true;
                    // 再获取该节点的出弧
                    arcs = graph.getOutArcs(s);
                    // 当前节点指向分支中的下一个节点
                    nodeID = s;
                    break;
                }
            }
        }while (hasBranch);
        return list;
    }

    @Override
    public NodeInfo getStartNode() {
        List<NodeInfo> startNodes = Linq.extEqualsList(this.getNodes(), "IsStart", true);
        int count = startNodes.size();
        if (count == 0) {
            throw new CannotFlowException("WorkflowDoesNotHaveStartNode");
        } else if (count > 1) {
            throw new CannotFlowException("WorkflowDoesNotHaveMultiStartNode");
        } else {
            return startNodes.get(0);
        }
    }

    /**
     * 获取所有结束节点
     * @return
     */
    @Override
    public List<NodeInfo> getEndNodes() {
        Collection<NodeInfo> collections = Collections.unmodifiableCollection(Linq.extEqualsList(this.getNodes(), "IsEnd", true));
        List<NodeInfo> list = new ArrayList<NodeInfo>();
        list.addAll(collections);
        return list;
    }

    @Override
    public boolean getKeep() {
        return this.keep;
    }
}
