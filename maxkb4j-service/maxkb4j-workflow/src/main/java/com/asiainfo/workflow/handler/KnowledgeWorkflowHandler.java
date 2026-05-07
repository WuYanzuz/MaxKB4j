package com.asiainfo.workflow.handler;

import com.asiainfo.knowledge.service.IKnowledgeActionService;
import com.asiainfo.workflow.enums.ActionStatus;
import com.asiainfo.workflow.enums.NodeStatus;
import com.asiainfo.workflow.exception.ExceptionResolverChain;
import com.asiainfo.workflow.model.KnowledgeWorkflow;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;
import com.asiainfo.workflow.registry.NodeCenter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
@Component
public class KnowledgeWorkflowHandler extends AbsWorkflowHandler {

    private final IKnowledgeActionService knowledgeActionService;

    public KnowledgeWorkflowHandler(NodeCenter nodeCenter,
                                     @Qualifier("workflowExecutor") Executor workflowExecutor,
                                     ExceptionResolverChain exceptionResolverChain,
                                     IKnowledgeActionService knowledgeActionService) {
        super(nodeCenter, workflowExecutor, exceptionResolverChain);
        this.knowledgeActionService = knowledgeActionService;
    }

    @Override
    public boolean canHandle(Workflow workflow) {
        return workflow instanceof KnowledgeWorkflow;
    }

    @Override
    public void execute(Workflow workflow) {
        if (workflow instanceof KnowledgeWorkflow knowledgeWorkflow) {
            List<AbsNode> nodes = knowledgeWorkflow.getStartNodes();
            runChainNodes(workflow, nodes);
            updateState(knowledgeWorkflow, ActionStatus.SUCCESS);
        }
    }

    public void updateState(Workflow workflow, ActionStatus actionStatus) {
        if (workflow instanceof KnowledgeWorkflow knowledgeWorkflow) {
            String actionId = knowledgeWorkflow.getKnowledgeParams().getActionId();
            knowledgeActionService.updateState(actionId,
                    knowledgeWorkflow.output().runtimeDetails(), actionStatus.name());
        }
    }

    @Override
    protected void onNodeStart(Workflow workflow, AbsNode node) {
        node.setStatus(NodeStatus.STARTED.getStatus());
        updateState(workflow, ActionStatus.STARTED);
    }
}