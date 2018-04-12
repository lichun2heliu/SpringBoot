package com.kotei.workflowengine.core;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/30
 * 修改历史：
 * 1. [2018/3/30]创建文件
 * 流程实例运行环境
 * @author chunl
 */
public interface RuntimeEnvironment {

    /**
     * 创建流程实例运行时上下文
     * @return
     */
    InstanceRuntimeContext createContext();
}
