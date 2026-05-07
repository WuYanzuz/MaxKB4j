package com.asiainfo.workflow.node.impl;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static com.asiainfo.workflow.enums.NodeType.PARAMETER_EXTRACTION;


public class ParameterExtractionNode extends AbsNode {

    public ParameterExtractionNode(String id,JSONObject properties) {
        super(id,properties);
        super.setType(PARAMETER_EXTRACTION.getKey());
    }

    @Override
    public void saveContext(Workflow workflow, Map<String, Object> detail) {
        context.put("result", detail.get("result"));
    }

    @Data
    public static class NodeParams {
        private String modelId;
        private JSONObject modelParamsSetting;
        private List<String> inputVariable;
        private List<Field> variableList;

    }

    @Data
    public static class Field {
        private String field;
        private String label;
        private String parameterType;
        private String desc;
    }
}
