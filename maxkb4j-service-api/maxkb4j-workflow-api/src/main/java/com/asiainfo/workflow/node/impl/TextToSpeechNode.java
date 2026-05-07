package com.asiainfo.workflow.node.impl;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static com.asiainfo.workflow.enums.NodeType.TEXT_TO_SPEECH;


public class TextToSpeechNode extends AbsNode {


    public TextToSpeechNode(String id,JSONObject properties) {
        super(id,properties);
        this.setType(TEXT_TO_SPEECH.getKey());
    }



    @Override
    public void saveContext(Workflow workflow, Map<String, Object> detail) {
        context.put("result", detail.get("result"));
    }

    @Data
    public static class NodeParams {
        private String ttsModelId;
        private List<String> contentList;
        private JSONObject modelParamsSetting;
        private Boolean isResult;
    }

}
