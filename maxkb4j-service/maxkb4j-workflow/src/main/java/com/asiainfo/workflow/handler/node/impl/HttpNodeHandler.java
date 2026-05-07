package com.asiainfo.workflow.handler.node.impl;

import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.asiainfo.application.executor.HttpRequestExecutor;
import com.asiainfo.common.domain.dto.ToolHttpRequest;
import com.asiainfo.workflow.annotation.NodeHandlerType;
import com.asiainfo.workflow.enums.NodeType;
import com.asiainfo.workflow.handler.node.AbsNodeHandler;
import com.asiainfo.workflow.model.NodeResult;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.model.params.HttpNodeParams;
import com.asiainfo.workflow.node.AbsNode;
import org.springframework.stereotype.Component;

import java.util.Map;

@NodeHandlerType(NodeType.HTTP_CLIENT)
@Component
public class HttpNodeHandler extends AbsNodeHandler {

    @Override
    protected NodeResult doExecute(Workflow workflow, AbsNode node) throws Exception {
        HttpNodeParams params = parseParams(node, HttpNodeParams.class);
        Map<String, Object> variables = workflow.getPromptVariables();
        HttpRequestExecutor executor = new HttpRequestExecutor(JSON.toJSONString(params));
        HttpResponse response = executor.execute(variables);
        ToolHttpRequest request = executor.getData();
        int resStatus = response.getStatus();
        String resBody = response.body();
        // 使用辅助方法写入详情
        putDetails(node, Map.of(
                "url", request.getUrl(),
                "method", request.getMethod(),
                "headers", request.getHeaders(),
                "requestBody", request,
                "params", request.getParams(),
                "timeout", request.getTimeout()
        ));
        return new NodeResult(Map.of("status", resStatus, "body", resBody));
    }
}