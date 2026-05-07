package com.asiainfo.workflow.node.impl;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.workflow.model.Condition;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static com.asiainfo.workflow.enums.NodeType.CONDITION;


public class ConditionNode extends AbsNode {

    public ConditionNode(String id,JSONObject properties) {
        super(id,properties);
        this.setType(CONDITION.getKey());
    }


    @Override
    public void saveContext(Workflow workflow, Map<String, Object> detail) {
        context.put("branchName", detail.get("branchName"));
    }

    @Data
    public static class NodeParams {
        private List<Branch> branch;
    }

    @Data
    public static class Branch {
        private String id;
        private String type;
        private String condition;
        private List<Condition> conditions;
    }

}
