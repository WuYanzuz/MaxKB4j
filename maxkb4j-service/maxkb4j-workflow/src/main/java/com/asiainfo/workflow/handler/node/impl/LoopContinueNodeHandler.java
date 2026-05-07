package com.asiainfo.workflow.handler.node.impl;

import com.asiainfo.workflow.annotation.NodeHandlerType;
import com.asiainfo.workflow.enums.NodeType;
import com.asiainfo.workflow.handler.node.AbsNodeHandler;
import com.asiainfo.workflow.model.NodeResult;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;
import com.asiainfo.workflow.node.impl.LoopContinueNode;
import com.asiainfo.workflow.util.ConditionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@NodeHandlerType(NodeType.LOOP_CONTINUE)
@Component
@RequiredArgsConstructor
public class LoopContinueNodeHandler extends AbsNodeHandler {

    private final ConditionUtil conditionUtil;

    @Override
    protected NodeResult doExecute(Workflow workflow, AbsNode node) throws Exception {
        LoopContinueNode.NodeParams params = parseParams(node, LoopContinueNode.NodeParams.class);
        boolean isContinue = conditionUtil.assertion(workflow, params.getCondition(), params.getConditionList());

        if (isContinue) {
            return new NodeResult(Map.of("is_continue", true, "branchId", "continue"));
        }
        return new NodeResult(Map.of("is_continue", false));
    }
}
