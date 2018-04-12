package com.kotei.workflowengine.controller.impl;

import com.kotei.workflowengine.controller.WorkflowService;
import com.kotei.workflowengine.core.impl.WorkflowEngineInstance;
import com.kotei.workflowengine.pojo.WFObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.Assert.notNull;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/30
 * 修改历史：
 * 1. [2018/3/30]创建文件
 * 定义工作流服务对客户端公开的操作
 * @author chunl
 */
public class WorkflowServiceImpl{

    @Autowired
    private WorkflowEngineInstance engine;

    /**
     * 发起一个新的流程
     *
     * @param mapID
     *            要发起的流程的流程图ID
     * @return 如果一个流程发起成功，则返回流程实例的ID
     *         此处如果不包装请求，那么客户端post请求内容mapID的时候，WCF会直接拒绝该请求，因为请求的正文不是json格式（
     *         同时参数又不是Stream）， 因为json数据类型规定了字符串必须加双引号，
     *         所以客户端post的mapID就必须要写成"mapID"才是合法的json数据类型字符串
     */
    @RequestMapping(value = "/Start", method = RequestMethod.POST)
    @ResponseBody
    public WFObject start(@RequestBody String mapID) {
        notNull(mapID,"必须传递mapID，才能开启流程");
        return engine.start(mapID);
    }

    /**
     * 流程实例流转到下一步
     *
     * @param map
     *            要流转的流程实例
     * @return 如果成功流转到下一步，则返回流转到的下一步的节点ID
     */

    public WFObject next(@RequestBody String map) {
        WFObject obj = new WFObject();
        //obj.setInstanceID(map.get("InstanceID"));
        notNull(obj,"流程跳转信息不能为空");
        return engine.next(obj);
    }

    /**
     * 以顺序的方式返回流程所有历史节点
     * @param instanceID
     * @return
     */
    @RequestMapping(value = "/GetHistoryNodeSequences", method = RequestMethod.GET)
    @ResponseBody
    public String[] getHistoryNodeSequences(String instanceID) {
        notNull(instanceID,"流程instanceID不能为空");
        return engine.getHistoryNodeSequences(instanceID);
    }

    /**
     * 传递instanceID返回流程当前节点
     * @param instanceID
     * @return
     */
    @RequestMapping(value = "/GetCurrent", method = RequestMethod.GET)
    @ResponseBody
    public WFObject getCurrent(String instanceID) {
        notNull(instanceID,"");
        return engine.getCurrent(instanceID);
    }



    /**
     * 获取所有起始节点
     * @param mapID
     * @return
     */
    @RequestMapping(value = "/GetStartNode", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getStartNode(String mapID) {
        Map<String, Object> map =new HashMap<String, Object>();
        notNull(mapID,"流程MapID不能为空");
        String result =engine.getStartNode(mapID);
        map.put("result", result);
        return map;
    }

    /**
     * 获取所有结束节点
     * @param mapID
     * @return
     */
    @RequestMapping(value = "/GetEndNodes", method = RequestMethod.GET)
    @ResponseBody
    public String[] getEndNodes(String mapID) {
        notNull(mapID,"流程MapID不能为空");
        String[] result = engine.getEndNodes(mapID);
        return result;
    }

    // link to <p>public WFObject next(@RequestBody String map)</p> method
    public WFObject next(WFObject obj) {
        return null;
    }
}
