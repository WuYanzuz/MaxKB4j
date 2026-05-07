package com.asiainfo.workflow.node.impl;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static com.asiainfo.workflow.enums.NodeType.REPLY;


public class DirectReplyNode extends AbsNode {

    public DirectReplyNode(String id,JSONObject properties) {
        super(id,properties);
        super.setType(REPLY.getKey());
    }

    @Override
    public void saveContext(Workflow workflow, Map<String, Object> detail) {
        context.put("answer", detail.get("answer"));
    }


    @Data
    public static class NodeParams {
        private String replyType;
        private List<String> fields;
        private String content;
        private Boolean isResult;
    }

}
