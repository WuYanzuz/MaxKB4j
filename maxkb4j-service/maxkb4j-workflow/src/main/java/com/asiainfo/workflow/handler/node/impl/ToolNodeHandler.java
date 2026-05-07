package com.asiainfo.workflow.handler.node.impl;

import com.asiainfo.application.executor.GroovyScriptExecutor;
import com.asiainfo.common.mp.entity.ToolInputField;
import com.asiainfo.oss.service.IOssService;
import com.asiainfo.workflow.annotation.NodeHandlerType;
import com.asiainfo.workflow.enums.NodeType;
import com.asiainfo.workflow.handler.node.AbsNodeHandler;
import com.asiainfo.workflow.model.NodeResult;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;
import com.asiainfo.workflow.node.impl.ToolNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

@NodeHandlerType({NodeType.TOOL, NodeType.TOOL_LIB})
@Component
@RequiredArgsConstructor
public class ToolNodeHandler extends AbsNodeHandler {

    private final IOssService fileService;

    @Override
    protected NodeResult doExecute(Workflow workflow, AbsNode node) throws Exception {
        ToolNode.NodeParams params = parseParams(node, ToolNode.NodeParams.class);
        Map<String, Object> execParams = new HashMap<>(5);
        execParams.put("fileService", fileService);
        if (!CollectionUtils.isEmpty(params.getInputFieldList())) {
            for (ToolInputField inputField : params.getInputFieldList()) {
                Object value = workflow.getFieldValue(inputField.getValue(), inputField.getSource());
                execParams.put(inputField.getName(), value);
            }
        }
        GroovyScriptExecutor scriptExecutor = new GroovyScriptExecutor(params.getCode(), params.getInitParams());
        Object result = scriptExecutor.execute(execParams);
        // 使用辅助方法写入详情
        putDetail(node, "params", execParams);
        if (Boolean.TRUE.equals(params.getIsResult())) {
            setAnswer(node, result.toString());
        }
        return new NodeResult(Map.of("result", result));
    }
}
