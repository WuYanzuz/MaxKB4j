package com.asiainfo.workflow.handler.node.impl;

import com.asiainfo.knowledge.consts.KnowledgeType;
import com.asiainfo.knowledge.dto.DocumentSimple;
import com.asiainfo.knowledge.service.IDocumentService;
import com.asiainfo.workflow.annotation.NodeHandlerType;
import com.asiainfo.workflow.enums.NodeType;
import com.asiainfo.workflow.handler.node.AbsNodeHandler;
import com.asiainfo.workflow.model.KnowledgeWorkflow;
import com.asiainfo.workflow.model.NodeResult;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;
import com.asiainfo.workflow.node.impl.KnowledgeWriteNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@NodeHandlerType(NodeType.KNOWLEDGE_WRITE)
@RequiredArgsConstructor
public class KnowledgeWriteHandler extends AbsNodeHandler {

    private final IDocumentService documentService;

    @SuppressWarnings("unchecked")
    @Override
    protected NodeResult doExecute(Workflow workflow, AbsNode node) throws Exception {
        KnowledgeWriteNode.NodeParams params = parseParams(node, KnowledgeWriteNode.NodeParams.class);
        Object value = workflow.getReferenceField(params.getDocumentList());
        putDetail(node, "write_content", value);

        if (workflow instanceof KnowledgeWorkflow knowledgeWorkflow) {
            boolean debug = knowledgeWorkflow.getKnowledgeParams().isDebug();
            if (!debug) {
                String knowledgeId = knowledgeWorkflow.getKnowledgeParams().getKnowledgeId();
                List<DocumentSimple> docs = (List<DocumentSimple>) value;
                documentService.batchCreateDocs(knowledgeId, KnowledgeType.WORKFLOW, docs);
            }
        }

        return new NodeResult(Map.of());
    }
}
