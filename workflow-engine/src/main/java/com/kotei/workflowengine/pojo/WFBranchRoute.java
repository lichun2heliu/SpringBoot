package com.kotei.workflowengine.pojo;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * 项目名称：phalaenopsis
 * 创建日期：2018/4/3
 * 修改历史：
 * 1. [2018/4/3]创建文件
 * 流程分支路线信息
 * @author chunl
 */
public class WFBranchRoute {

    /**
     * 流程是从那个节点分支的
     */
    @Getter
    @Setter
    public String NodeID;

    /**
     * 流程走的是节点的哪个分支
     */
    @Getter
    @Setter
    public String BranchName;

    /**
     * 流程走到分支的第几个节点了（从0开始）
     */
    @Setter
    @Getter
    public int Index;

    /**
     * 转换为json字符串进行存储
     * @return
     */
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static WFBranchRoute from(String route) {
        if (route == null || route.equals("")){
            return null;
        }

        return JSON.parseObject(route, WFBranchRoute.class);
    }

}
