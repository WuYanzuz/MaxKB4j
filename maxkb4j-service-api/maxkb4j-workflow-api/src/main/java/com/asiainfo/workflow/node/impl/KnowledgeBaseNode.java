package com.asiainfo.workflow.node.impl;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;

import java.util.Map;

import static com.asiainfo.workflow.enums.NodeType.KNOWLEDGE_BASE;

public class KnowledgeBaseNode extends AbsNode {
    public KnowledgeBaseNode(String id, JSONObject properties) {
        super(id, properties);
        super.setType(KNOWLEDGE_BASE.getKey());
    }

    @Override
    public void saveContext(Workflow workflow, Map<String, Object> detail) {

    }
}
