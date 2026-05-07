package com.asiainfo.workflow.service;

import com.asiainfo.workflow.logic.LfNode;
import com.asiainfo.workflow.node.AbsNode;

/**
 * 节点创建器接口
 * 定义节点创建的标准方法
 */
public interface INodeCreator {

    /**
     * 创建节点实例
     *
     * @param lfNode 前端节点数据
     * @return 节点实例
     */
    AbsNode createNode(LfNode lfNode);

}