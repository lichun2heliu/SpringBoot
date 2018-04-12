package com.kotei.workflowengine.storage.impl;

import com.kotei.workflowengine.pojo.WFHistory;
import com.kotei.workflowengine.pojo.WFInstance;
import com.kotei.workflowengine.storage.WorkflowStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/3
 * 修改历史：
 * 1. [2018/4/3]创建文件
 *
 * @author chunl
 */
@Service("wfOracleDBStorage")
public class WFOracleDBStorage implements WorkflowStorage {

    public WFOracleDBStorage() {
        int i = 0;
    }

    //@Autowired
    //private WorkFlowDao workflowDao;

    @Override
    @Transactional
    public void create(WFInstance instance, WFHistory history) {
        this.save(instance, history);
    }

    @Override
    @Transactional
    public void save(WFInstance instance, WFHistory history) {
//        workflowDao.saveOrUpdateWFInstance(instance);
//        workflowDao.saveWFHistory(history);
    }

    @Override
    @Transactional
    public WFInstance getInstance(String instanceID) {
        //return workflowDao.getInstance(instanceID);
        return null;
    }

    @Override
    @Transactional
    public List<WFHistory> getHistories(String instanceID) {
        //return workflowDao.getHistories(instanceID);
        return null;
    }

    @Override
    @Transactional
    public void delete(String instanceID) {
//        workflowDao.deleteInstance(instanceID);
//        workflowDao.deleteHistory(instanceID);
    }

    @Override
    @Transactional
    public void saveAndRemoveLastHistory(WFInstance instance, WFHistory lastHistory) {
        //workflowDao.saveOrUpdateWFInstance(instance);
        //workflowDao.deleteLastHistory(lastHistory);
    }
}
