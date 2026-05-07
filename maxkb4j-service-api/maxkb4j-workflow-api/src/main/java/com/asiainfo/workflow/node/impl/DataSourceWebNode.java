package com.asiainfo.workflow.node.impl;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;

import java.util.Map;

import static com.asiainfo.workflow.enums.NodeType.DATA_SOURCE_WEB;


public class DataSourceWebNode extends AbsNode {
    public DataSourceWebNode(String id, JSONObject properties) {
        super(id, properties);
        this.setType(DATA_SOURCE_WEB.getKey());
    }

    @Override
    public void saveContext(Workflow workflow, Map<String, Object> detail) {
    }
}
