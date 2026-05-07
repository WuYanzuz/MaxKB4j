package com.asiainfo.workflow.handler.node.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.workflow.annotation.NodeHandlerType;
import com.asiainfo.workflow.enums.NodeType;
import com.asiainfo.workflow.handler.node.AbsNodeHandler;
import com.asiainfo.workflow.model.NodeResult;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;
import com.asiainfo.workflow.node.impl.FormNode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@NodeHandlerType(NodeType.FORM)
@Component
public class FormNodeHandler extends AbsNodeHandler {

    @Override
    protected NodeResult doExecute(Workflow workflow, AbsNode node) throws Exception {
        FormNode.NodeParams params = parseParams(node, FormNode.NodeParams.class);
        Map<String, Object> formData = params.getFormData();
        Map<String, Object> nodeVariable = new HashMap<>();

        if (formData != null) {
            nodeVariable.put("is_submit", true);
            nodeVariable.put("form_data", formData);
            nodeVariable.putAll(formData);
            putDetail(node, "form_data", formData);
        } else {
            JSONArray formFieldList = params.getFormFieldList();
            JSONObject formSetting = new JSONObject();
            formSetting.put("form_field_list", formFieldList);
            String formRender = "<form_render>" + formSetting + "</form_render>";
            String formContentFormat = params.getFormContentFormat();
            String answerText = workflow.renderPrompt(formContentFormat, Map.of("form", formRender));
            setAnswer(node, answerText);
            nodeVariable.put("form_field_list", formFieldList);
            nodeVariable.put("form_content_format", formContentFormat);
            nodeVariable.put("is_submit", false);
        }
        return new NodeResult(nodeVariable, false, this::shouldInterrupt);
    }

    @Override
    public boolean shouldInterrupt(AbsNode node) {
        return !(boolean) node.getContext().getOrDefault("is_submit", false);
    }
}
