package com.asiainfo.workflow.handler.node.impl;

import com.asiainfo.workflow.annotation.NodeHandlerType;
import com.asiainfo.workflow.enums.NodeType;
import com.asiainfo.workflow.handler.node.AbsNodeHandler;
import com.asiainfo.workflow.model.NodeResult;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;
import com.asiainfo.workflow.node.impl.LoopBreakNode;
import com.asiainfo.workflow.util.ConditionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@NodeHandlerType(NodeType.LOOP_BREAK)
@Component
@RequiredArgsConstructor
public class LoopBreakNodeHandler extends AbsNodeHandler {

    private final ConditionUtil conditionUtil;

    @Override
    protected NodeResult doExecute(Workflow workflow, AbsNode node) throws Exception {
        LoopBreakNode.NodeParams params = parseParams(node, LoopBreakNode.NodeParams.class);
        boolean isBreak = conditionUtil.assertion(workflow, params.getCondition(), params.getConditionList());
        putDetail(node, "is_break", isBreak);
        if (isBreak) {
            setAnswer(node, "BREAK");
        }
        return new NodeResult(Map.of());
    }
}
