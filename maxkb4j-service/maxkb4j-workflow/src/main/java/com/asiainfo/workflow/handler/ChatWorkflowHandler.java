package com.asiainfo.workflow.handler;

import com.asiainfo.common.domain.dto.ChatMessageVO;
import com.asiainfo.workflow.exception.ExceptionResolverChain;
import com.asiainfo.workflow.model.KnowledgeWorkflow;
import com.asiainfo.workflow.model.NodeResultFuture;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;
import com.asiainfo.workflow.registry.NodeCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Slf4j
@Component
public class ChatWorkflowHandler extends AbsWorkflowHandler {

    public ChatWorkflowHandler(NodeCenter nodeCenter,
                               @Qualifier("workflowExecutor") Executor workflowExecutor,
                               ExceptionResolverChain exceptionResolverChain) {
        super(nodeCenter, workflowExecutor, exceptionResolverChain);
    }

    @Override
    public boolean canHandle(Workflow workflow) {
        // ChatWorkflowHandler handles all workflows except KnowledgeWorkflow
        return !(workflow instanceof KnowledgeWorkflow);
    }

    @Override
    protected NodeResultFuture handleNodeError(Workflow workflow, AbsNode node, Exception ex) {
        NodeResultFuture result = super.handleNodeError(workflow, node, ex);
        emitErrorToSink(workflow, node, ex);
        return result;
    }

    /**
     * Sends an error message to the workflow's sink if applicable.
     *
     * @param workflow the workflow context
     * @param node     the node that failed
     * @param ex       the exception that occurred
     */
    protected void emitErrorToSink(Workflow workflow, AbsNode node, Exception ex) {
        if (workflow.getChatParams() != null
                && workflow.output().needsSink()) {
            ChatMessageVO errMessage = node.toChatMessageVO(
                    workflow.getChatParams().getChatId(),
                    workflow.getChatParams().getChatRecordId(),
                    String.format("Exception: %s", ex.getMessage()),
                    "",
                    null,
                    true);
            workflow.output().emit(errMessage);
        }
    }
}