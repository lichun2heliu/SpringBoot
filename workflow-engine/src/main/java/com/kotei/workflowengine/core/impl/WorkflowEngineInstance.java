package com.kotei.workflowengine.core.impl;

import com.kotei.core.utils.collection.Linq;
import com.kotei.workflowengine.bootstrap.WorkflowsConfigurationLoader;
import com.kotei.workflowengine.core.*;
import com.kotei.workflowengine.pojo.WFInstance;
import com.kotei.workflowengine.pojo.WFObject;
import com.kotei.workflowengine.storage.WorkflowStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/2
 * 修改历史：
 * 1. [2018/4/2]创建文件
 *
 * @author chunl
 */
@Component
public class WorkflowEngineInstance implements RuntimeEnvironment {

    private Logger logger = LoggerFactory.getLogger(WorkflowEngineInstance.class);

    /**
     * 流程引擎包含的所有流程图
     */
    private WorkflowMapCollection mapCollection;

    /**
     * 流程数据存储程序的创建器
     */
    public WorkflowStorage StorageBuilder;

    @Autowired
    private WorkflowStorage storage;

    public WorkflowStorage getStorageBuilder() {
        return StorageBuilder;
    }

    public WorkflowEngineInstance() {
        mapCollection = new WorkflowMapCollection();
        ClassPathResource resource = new ClassPathResource("config/workflow-config.xml");
        String path;
        try {
            path = resource.getFile().getAbsolutePath();
            loadFile(path);
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    /**
     * 从配置文件中加载流程配置
     *
     * @param configFile 流程图的xml配置文件路径
     */
    public void loadFile(String configFile) {
        // 以静态配置文件加载工作流
        load(configFile);
    }

    /**
     * 从xml字符串中加载流程图配置
     *
     * @param xml
     */
    public void loadXml(String xml) {

    }

    /**
     * 从配置文件中加载流程图集合
     *
     * @param xmlFile xml配置文件路径
     */
    private void load(String xmlFile) {
        try {
            if (new File(xmlFile).exists()) {
                WorkflowsConfigurationLoader loader = new WorkflowsConfigurationLoader();
                WorkflowMapCollection workflowMapCollection = loader.loadFile(xmlFile);
                if (workflowMapCollection != null) {
                    List<WorkflowMap> list = workflowMapCollection.getEnumerator();
                    for (WorkflowMap map : list) {
                        mapCollection.add(map);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("", e);
        } catch (Exception e) {
            logger.error("", e);
        }
    }


    /**
     * 发起一个新的流程
     *
     * @param mapID 要发起的流程的流程图ID
     * @return 如果一个流程发起成功，则返回流程实例的ID
     */
    public WFObject start(String mapID) {
        if (!mapCollection.contains(mapID)) {
            return null;
        }

        WorkflowMap workflowMap = (WorkflowMap) mapCollection.get(mapID);  //需要替换的代码

        WorkflowInstance instance = workflowMap.createInstance(this);

        WFObject wfObject = new WFObject();
        wfObject.setInstanceID(instance.getInstanceID());
        wfObject.setCurrentNode(instance.getCurrent());
        wfObject.setIsFinished(instance.getFinished());
        return wfObject;
    }

    /**
     * 流程实例流转到下一步 返回与参数相同的同一个WFObject对象，更新其对应的属性，使得可以连续调用的功能
     *
     * @param obj 要流转的流程实例
     * @return 如果成功流转到下一步，则返回流转到的下一步的节点ID
     */
    public WFObject next(WFObject obj) {
        if (obj == null) {
            return obj;
        }

        WorkflowInstance instance = getInstance(obj.InstanceID);
        if (instance == null) {
            return null;
        }

        // 当前节点
        String current = instance.getCurrent();

        String next = instance.next();
        if (next == instance.getMap().getFinishedNode()) {
            // 流程结束，更新CurrentNode为最后一个结束的节点而不是FinishedNode，更符合逻辑
            // 表示流程是从该节点结束的
            obj.CurrentNode = current;
            obj.IsFinished = true;
        } else {
            // 更新当前节点
            obj.CurrentNode = next;
            obj.IsFinished = false;
        }

        return obj;
    }

    public WFObject previous(WFObject obj) {
        if (obj != null) {
            WorkflowInstance instance = getInstance(obj.InstanceID);

            if (instance == null) {
                return null;
            }

            String previous = instance.previous();
            obj.CurrentNode = previous;
            obj.IsFinished = false;
        }
        return obj;
    }

    public String[] getHistoryNodeSequences(String instanceID) {
        WorkflowInstance instance = getInstance(instanceID);
        if (instance == null) {
            return null;
        }
        List<String> list = Linq.extSelect(instance.getHistoryNodeSequences(), "NodeID");
        return list.toArray(new String[list.size()]);
    }

    public String getStartNode(String mapID) {
        if (!mapCollection.contains(mapID)) {
            return null;
        }

        WorkflowMap map = mapCollection.get(mapID);
        return map.getStartNode().NodeID;
    }

    public String[] getEndNodes(String mapID) {
        if (!mapCollection.contains(mapID)) {
            return null;
        }

        WorkflowMap map = mapCollection.get(mapID);
        List<String> list = Linq.extSelect(map.getEndNodes(), "NodeID");
        return (String[]) list.toArray(new String[list.size()]);
    }

    public WFObject getCurrent(String instanceID) {
        WorkflowInstance instance = getInstance(instanceID);
        if (instance == null) {
            return null;
        }

        String current = instance.getCurrent();
        if (instance.getCurrent().equals(instance.getMap().getFinishedNode())) {
            List<NodeInfo> histories = instance.getHistoryNodeSequences();
            current = histories.get(histories.size() - 1).NodeID;
        }

        WFObject wfObject = new WFObject();
        wfObject.setInstanceID(instance.getInstanceID());
        wfObject.setCurrentNode(current);
        wfObject.setIsFinished(instance.getFinished());
        return wfObject;
    }

    public void delete(String instanceID) {
        WorkflowStorage storage = getStorageBuilder();
        storage.delete(instanceID);
    }

    /**
     * 绑定传递给流程节点的数据
     *
     * @param dataContext
     * @param obj
     */
    private void bindDataContext(NodeDataContext dataContext, WFObject obj) {
        // 设置服务端传递的数据
        dataContext.ServerData = obj.ServerData;
        if (obj.getServerNamedDatas() != null && obj.getServerNamedDatas().size() > 0) {
            dataContext.getServerNamedDatas().addAll(obj.getServerNamedDatas());
        }
    }

    private WorkflowInstance getInstance(String instanceID) {
        WFInstance obj = storage.getInstance(instanceID);
        if (obj == null) {
            return null;
        }

        WorkflowMap map = mapCollection.get(obj.getMapID());
        WorkflowInstance instance = map.getInstance(obj, this);
        return instance;
    }

    @Override
    public InstanceRuntimeContext createContext() {
        InstanceRuntimeContext context = new InstanceRuntimeContext();
        context.setStorage(getStorageBuilder());
        return context;
    }
}
