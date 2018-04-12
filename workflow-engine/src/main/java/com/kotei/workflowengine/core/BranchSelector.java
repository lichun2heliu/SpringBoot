package com.kotei.workflowengine.core;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/3/30
 * 修改历史：
 * 1. [2018/3/30]创建文件
 *
 * @author chunl
 */
public interface BranchSelector {

    /**
     *
     * @param dataContext
     * @param currentNode 当前节点
     * @param currentBranch 流转到当前节点时，所处的分支
     * @return 返回选择的分支name
     */
    String selectBranch(NodeDataContext dataContext, String currentNode, String currentBranch);
}
