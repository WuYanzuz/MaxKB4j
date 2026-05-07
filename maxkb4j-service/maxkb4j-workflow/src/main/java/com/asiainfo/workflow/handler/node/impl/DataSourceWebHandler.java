package com.asiainfo.workflow.handler.node.impl;

import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.knowledge.dto.DocumentSimple;
import com.asiainfo.knowledge.service.IDocumentWebService;
import com.asiainfo.workflow.annotation.NodeHandlerType;
import com.asiainfo.workflow.enums.NodeType;
import com.asiainfo.workflow.handler.node.AbsNodeHandler;
import com.asiainfo.workflow.model.*;
import com.asiainfo.workflow.node.AbsNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@NodeHandlerType(NodeType.DATA_SOURCE_WEB)
@RequiredArgsConstructor
public class DataSourceWebHandler extends AbsNodeHandler {

    private final IDocumentWebService documentWebService;

    @Override
    protected NodeResult doExecute(Workflow workflow, AbsNode node) throws Exception {
        List<DocumentSimple> documentList = new ArrayList<>();
        Map<String, Object> inputParams = new HashMap<>();
        if (workflow instanceof KnowledgeWorkflow knowledgeWorkflow) {
            KnowledgeParams knowledgeParams = knowledgeWorkflow.getKnowledgeParams();
            DataSource dataSource = knowledgeParams.getDataSource();
            if (dataSource != null) {
                String sourceUrl = dataSource.getSourceUrl();
                String selector = dataSource.getSelector() == null ? "body" : dataSource.getSelector();
                inputParams.put("sourceUrl", sourceUrl);
                inputParams.put("selector", selector);
                documentList = documentWebService.getDocumentList(sourceUrl, selector, true);
            }
        }
        putDetails(node, Map.of(
                "inputParams", inputParams,
                "outputParams", documentList
        ));
        return new NodeResult(Map.of("documentList", documentList));
    }
}
