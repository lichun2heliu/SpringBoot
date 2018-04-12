package com.kotei.workflowengine.core;

import com.kotei.core.utils.collection.Linq;
import com.kotei.workflowengine.exception.KeyNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.*;


/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/30
 * 修改历史：
 * 1. [2018/3/30]创建文件
 * <K> 节点的key
 * <W> 弧的权值
 * <NodeInfo> 节点的value
 *
 * @author chunl
 */
public class NetGraph {

    private class GraphNode {
        public GraphNode() {
            this.inArcs = new LinkedList<WeightArc>();
            this.outArcs = new LinkedList<WeightArc>();
        }

        /**
         * 节点的key
         */
        @Getter
        @Setter
        private String key;

        /**
         * 节点的value
         */
        @Setter
        @Getter
        private NodeInfo value;

        /**
         * 节点的入弧
         */
        @Getter
        private LinkedList<WeightArc> inArcs;

        /**
         * 节点的出弧
         */
        @Getter
        private LinkedList<WeightArc> outArcs;

    }

    /**
     * 带权值的弧
     * String 弧头节点的key
     * String 弧的权值
     */
    private class WeightArc {

        /**
         * 弧所指向的节点的key
         */
        @Getter
        private String key;

        /**
         * 弧的权值集合
         */
        @Getter
        private Set<String> weights;

        public WeightArc(String key) {
            this.key = key;
            this.weights = new TreeSet<String>();
        }
    }

    /**
     * 弧的类型
     */
    public enum ArcType {

        IN(1), OUT(~1);
        private int value;

        ArcType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 双邻接表
     */
    private Map<String, GraphNode> adjList;

    private final String ERROR_TIPS = "key can not empty";

    private final String WORKFLOW_ERROR_TIPS = "workflow node can not be empty";

    private final String ARC_ERROR_TIPS = "arc can not be empty";

    /**
     * 好像只需要出边表，而不需要入边
     */
    public NetGraph() {
        // typeof(W).ThrowIfNotImplementInterface<IComparable<W>>();
        adjList = new HashMap<String, GraphNode>();
    }

    /**
     * 添加一个节点
     *
     * @param key
     * @param value
     */
    public void addNode(String key, NodeInfo value) {
        addNode(key, value, null, null);
    }

    public void addNode(String key, NodeInfo value, List<String> inArcs, List<String> outArcs) {
        Assert.notNull(key, WORKFLOW_ERROR_TIPS);
        GraphNode node = new GraphNode();
        node.setKey(key);
        node.setValue(value);
        adjList.put(node.getKey(), node);
        addArcsCore(node, inArcs, ArcType.IN);
        addArcsCore(node, outArcs, ArcType.OUT);
    }

    /**
     * remove node by key 删除一个节点
     *
     * @param key
     * @return delete node if exist。seccuss returns true, else returns false
     */
    public boolean removeNode(String key) {
        Assert.notNull(key, WORKFLOW_ERROR_TIPS);
        GraphNode node = null;
        if (adjList.containsKey(key)) {
            node.inArcs.clear();
            node.outArcs.clear();
            adjList.remove(key);
            return true;
        }
        return false;
    }

    /**
     * remove graph all nodes
     */
    public void clearNodes() {
        for (Map.Entry<String, GraphNode> item : adjList.entrySet()) {
            item.getValue().inArcs.clear();
            item.getValue().outArcs.clear();
        }
        adjList.clear();
    }

    /**
     * get some node. if node not exist, else return default <code>V</code>
     *
     * @param key
     * @return
     */
    public NodeInfo getNode(String key) {
        Assert.notNull(key);
        GraphNode node = null;
        if (adjList.containsKey(key)) {
            return (NodeInfo) adjList.get(key).getValue();
        } else {
            return null;
        }
    }

    /**
     * return all nodes
     *
     * @return
     */
    public List<NGraphNode> getAllNodes() {
        List<NGraphNode> list = new ArrayList<NGraphNode>();
        for (Map.Entry<String, GraphNode> item : adjList.entrySet()) {
            list.add(new NGraphNode(item.getKey(), item.getValue().getValue()));
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * 获取某个节点的所有入弧节点
     *
     * @param key
     * @return
     */
    public List<NGraphNode> getInNodes(String key) {
        return Collections.unmodifiableList(getNodes(key, ArcType.IN));
    }

    /**
     * 获取某个节点的所有出弧节点
     *
     * @param key
     * @return
     */
    public List<NGraphNode> getOutNodes(String key) {
        return Collections.unmodifiableList(getNodes(key, ArcType.OUT));
    }

    private List<NGraphNode> getNodes(String key, ArcType arcType) {
        Assert.notNull(key, ERROR_TIPS);
        List<String> nodeKeys = null;
        nodeKeys = getArcs(key, arcType);

        List<NGraphNode> list = new ArrayList<NGraphNode>();
        for (String k : nodeKeys) {
            list.add(new NGraphNode(k, adjList.get(k).getValue()));
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * 给某个节点添加入弧
     *
     * @param key
     * @param inArcs
     */
    public void addInArcs(String key, String... inArcs) {
        addArcs(key, ArcType.IN, inArcs);
    }

    /**
     * 给某个节点添加出弧
     *
     * @param key
     * @param outArcs
     */
    public void addOutArcs(String key, String... outArcs) {
        addArcs(key, ArcType.OUT, outArcs);
    }

    private void addArcs(String key, ArcType type, String... arcs) {
        Assert.notNull(key, ERROR_TIPS);
        if (arcs != null && arcs.length > 0) {
            GraphNode node = null;

            if (adjList.containsKey(key)) {
                node = adjList.get(key);
                List<String> arcslist = java.util.Arrays.asList(arcs);
                addArcsCore(node, arcslist, type);
                // 节点A添加了出弧指向B，同时意味着节点B添加了入弧来自A
                for (String item : arcs) {
                    GraphNode node2 = adjList.get(item);
                    List<String> kv = new ArrayList<>();
                    kv.add(key);
                    ArcType type2 = type == ArcType.IN ? ArcType.OUT : ArcType.IN;
                    // addArcsCore(node2, kv, ~type.getValue());
                    addArcsCore(node2, kv, type2);
                }
            } else {
                throw new KeyNotFoundException();
            }
        }
    }

    /**
     * 移除某个节点的指定入弧
     *
     * @param key
     * @param inArcs
     */
    public void removeInArcs(String key, String... inArcs) {
        removeArcs(key, ArcType.IN, inArcs);
    }

    /**
     * 移除某个节点的指定出弧
     *
     * @param key
     * @param outArcs
     */
    public void removeOutArcs(String key, String... outArcs) {
        removeArcs(key, ArcType.OUT, outArcs);
    }

    private void removeArcs(String key, ArcType type, String... arcs) {
        Assert.notNull(key);
        GraphNode node = null;
        if (adjList.containsKey(key)) {
            node = adjList.get(key);
            List<String> arclist = java.util.Arrays.asList(arcs);
            removeArcsCore(node, arclist, type);
        } else
            throw new KeyNotFoundException();
    }

    /**
     * 清除某个节点所有入弧
     *
     * @param key
     */
    public void clearInArcs(String key) {
        clearArcs(key, ArcType.IN);
    }

    /**
     * 清除某个节点的所有出弧
     *
     * @param key
     */
    public void clearOutArcs(String key) {
        clearArcs(key, ArcType.OUT);
    }

    /**
     * 清除某个节点的所有弧
     *
     * @param key
     */
    public void clearArcs(String key) {
        clearInArcs(key);
        clearOutArcs(key);
    }


    private void clearArcs(String key, ArcType type) throws KeyNotFoundException {
        Assert.notNull(key);
        GraphNode node = null;
        if (adjList.containsKey(key)) {
            node = adjList.get(key);
            clearArcsCore(node, type);
        } else
            throw new KeyNotFoundException();
    }

    /**
     * 重新设置某个节点的所有入弧
     *
     * @param key
     * @param inArcs
     */
    public void setInArcs(String key, String... inArcs) {
        setArcs(key, ArcType.IN, inArcs);
    }

    /**
     * 重新设置某个节点的所有出弧
     *
     * @param key
     * @param outArcs
     */
    public void setOutArcs(String key, String... outArcs) {
        setArcs(key, ArcType.OUT, outArcs);
    }

    private void setArcs(String key, ArcType type, String... arcs) {
        Assert.notNull(key, ERROR_TIPS);
        clearArcs(key, type);
        addArcs(key, type, arcs);
    }

    /**
     * 获取某个节点的所有入弧
     *
     * @param key
     * @return
     */
    public List<String> getInArcs(String key) {
        return Collections.unmodifiableList(getArcs(key, ArcType.IN));
    }

    /**
     * 获取某个节点的所有出弧
     *
     * @param key
     * @return
     */
    public List<String> getOutArcs(String key) {
        return getArcs(key, ArcType.OUT);
    }

    private List<String> getArcs(String key, ArcType arcType) {
        Assert.notNull(key, ERROR_TIPS);
        GraphNode node = null;
        if (adjList.containsKey(key)) {
            node = adjList.get(key);
            LinkedList<WeightArc> getArcs = null;
            if (arcType == ArcType.IN) {
                getArcs = node.inArcs;
            } else if (arcType == ArcType.OUT) {
                getArcs = node.outArcs;
            }

            List<String> list = new ArrayList<String>();
            for (WeightArc arc : getArcs) {
                list.add(arc.key);
            }
            return Collections.unmodifiableList(list);
        } else {
            throw new KeyNotFoundException();
        }
    }

    /**
     * 获取某一条弧，然后再给弧添加、删除权值
     * 给弧添加权值 获取权值 删除权值 判断权值是否存在
     * 获取节点key1（弧尾）指向节点key2（弧头）的弧
     *
     * @param tail
     * @param head
     * @return
     */
    public Set<String> getArc(String tail, String head) {
        return getArc(tail, head, ArcType.OUT);
    }

    private Set<String> getArc(String nodeKey, String arcKey, ArcType arcType) {
        Assert.notNull(nodeKey, ERROR_TIPS);
        GraphNode node;
        if (adjList.containsKey(nodeKey)) {
            node = adjList.get(nodeKey);
            if (arcType == ArcType.IN) {
                WeightArc arc = Linq.extEquals(node.inArcs, "key", arcKey);
                if (null == arc) {
                    // 没有该弧
                    return null;
                }
                return arc.weights;
            } else if (arcType == ArcType.OUT) {
                WeightArc arc = Linq.extEquals(node.outArcs, "key", arcKey);
                if (null == arc) {
                    // 没有该弧
                    return null;
                }
                return arc.weights;
            }
            return null;
        } else {
            throw new KeyNotFoundException();
        }
    }

    private void addArcsCore(GraphNode node, List<String> arcs, ArcType arcType) {
        if (null != arcs) {
            // 将现有的弧添加到集中
            Set<String> sets = new HashSet<String>();
            if (arcType == ArcType.IN && node.inArcs.size() > 0) {
                for (WeightArc arc : node.inArcs) {
                    sets.add(arc.key);
                }
            } else if (arcType == ArcType.OUT && node.outArcs.size() > 0) {
                for (WeightArc arc : node.outArcs) {
                    sets.add(arc.key);
                }
            }

            // add new arc
            for (String item : arcs) {
                Assert.notNull(item, ARC_ERROR_TIPS);
                if (!adjList.containsKey(item)) {
                    throw new KeyNotFoundException();
                }

                if (sets.contains(item)) {
                    // 忽略重复添加已经存在的弧
                    continue;
                }

                WeightArc arc = new WeightArc(item);
                if (arcType == ArcType.IN) {
                    node.inArcs.addLast(arc);
                } else if (arcType == ArcType.OUT) {
                    node.outArcs.addLast(arc);
                }
                sets.add(item);
            }
        }
    }

    private void removeArcsCore(GraphNode node, List<String> arcs, ArcType arcType) {
        if (null != arcs) {
            Set<String> sets = new HashSet<String>(arcs);

            List<WeightArc> deletingList = new ArrayList<WeightArc>(); // 待删除列表
            if (arcType == ArcType.IN) {
                for (WeightArc arc : node.inArcs) {
                    // 判断当前节点是否要被删除
                    if (sets.contains(arc.key)) {
                        // 遍历的时候不能对集合进行修改
                        deletingList.add(arc);
                    }
                }

                LinkedList<WeightArc> item = new LinkedList<WeightArc>();
                for (WeightArc weightArc : item) {
                    if (!sets.contains(weightArc.key)) {
                        item.add(weightArc);
                    }
                }
                node.inArcs = item;
            } else if (arcType == ArcType.OUT) {
                for (WeightArc arc : node.outArcs) {
                    if (sets.contains(arc.key)) {
                        deletingList.add(arc);
                    }
                }
            }

            if (deletingList.size() > 0) {
                if (arcType == ArcType.IN) {
                    for (WeightArc arc : deletingList) {
                        node.inArcs.remove(arc);
                    }
                } else if (arcType == ArcType.OUT) {
                    for (WeightArc arc : deletingList) {
                        node.outArcs.remove(arc);
                    }
                }
            }
        }
    }

    private void clearArcsCore(GraphNode node, ArcType arcType) {
        if (arcType == ArcType.IN) {
            node.inArcs.clear();
        } else if (arcType == ArcType.OUT) {
            node.outArcs.clear();
        }
    }

    /**
     * 获取或修改节点的值。获取时，如果节点不存在，则引发KeyNotFoundException；设置时，如果节点不存在，则添加新节点（孤立节点）。
     *
     * @param key
     * @return
     * @throws KeyNotFoundException
     */
    public NodeInfo getThis(String key) {
        Assert.notNull(key, ERROR_TIPS);
        if (adjList.containsKey(key)) {
            return adjList.get(key).getValue();
        } else {
            // 不能返回null，也不能返回default(NodeInfo)：如果泛型类型NodeInfo是值类型，则返回值可能会对开发人员产生误解，
            // 因为实际上并不存在该key
            throw new KeyNotFoundException();
        }
    }

    public void setThis(String key, NodeInfo value) {
        Assert.notNull(key, ERROR_TIPS);
        if (adjList.containsKey(key)) {
            adjList.get(key).setValue(value);
        } else {
            addNode(key, value);
        }
    }

    /**
     * 判断图中是否包含指定的节点
     *
     * @param key
     * @return
     */
    public boolean Contains(String key) {
        Assert.notNull(key, ERROR_TIPS);
        return adjList.containsKey(key);
    }

    public int getCount() {
        return adjList.size();
    }

    @Getter
    @Setter
    public static class NGraphNode {

        public NGraphNode(String key, NodeInfo value) {
            super();
            this.key = key;
            this.value = value;
        }

        private String key;

        private NodeInfo value;
    }
}
