package com.kotei.workflowengine.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/3
 * 修改历史：
 * 1. [2018/4/3]创建文件
 *
 * @author chunl
 */
public class WorkflowMapCollection {

    /**
     *  流程图集合
     */
    private Map<String, WorkflowMap> mapCollection;

    public WorkflowMapCollection() {

    }

    public void add(WorkflowMap item) {
        if (item != null) {
            mapCollection.put(item.getMapID(), item);
        }
    }

    public void clear() {
        mapCollection.clear();
    }

    public boolean contains(WorkflowMap item) {
        if (item != null) {
            return mapCollection.containsKey(item.getMapID());
        }
        return false;
    }

    public boolean contains(String mapID) {
        return mapCollection.containsKey(mapID);
    }

    public void copyTo(WorkflowMap[] array, int arrayIndex) {
        WorkflowMap[] maps = new WorkflowMap[arrayIndex + mapCollection.size()];
        System.arraycopy(array, 0, maps, 0, arrayIndex);
        System.arraycopy(mapCollection.values(), 0, maps, arrayIndex, mapCollection.size());
        array = maps;
    }

    public int count() {
        return mapCollection.size();
    }

    public boolean isReadOnly() {
        return false;
    }

    public boolean remove(WorkflowMap item) {
        if (item != null) {
            return mapCollection.remove(item.getMapID()) != null ? true : false;
        }
        return false;
    }

    public List<WorkflowMap> getEnumerator() {
        List<WorkflowMap> result = new ArrayList<WorkflowMap>();

        if (null == mapCollection){
            return result;
        }

        if (null == mapCollection.entrySet()){
            return result;
        }

        if (0 == mapCollection.entrySet().size()){
            return result;
        }

        for (Map.Entry<String, WorkflowMap> item : mapCollection.entrySet()) {
            result.add(item.getValue());
        }
        return result;
    }

    /**
     * 获取mapID对应的流程图
     * @param mapID
     * @return
     */
    public WorkflowMap get(String mapID) {
        if (mapID == null || mapID.equals("")) {
            return null;
        }
        return mapCollection.get(mapID);
    }


}
