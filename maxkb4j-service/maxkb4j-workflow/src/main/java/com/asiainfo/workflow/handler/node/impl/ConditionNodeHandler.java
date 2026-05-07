package com.asiainfo.workflow.handler.node.impl;

import com.asiainfo.workflow.annotation.NodeHandlerType;
import com.asiainfo.workflow.enums.NodeType;
import com.asiainfo.workflow.handler.node.AbsNodeHandler;
import com.asiainfo.workflow.model.NodeResult;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.model.params.ConditionNodeParams;
import com.asiainfo.workflow.node.AbsNode;
import com.asiainfo.workflow.util.ConditionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@NodeHandlerType(NodeType.CONDITION)
@Component
@RequiredArgsConstructor
public class ConditionNodeHandler extends AbsNodeHandler {

    private final ConditionUtil conditionUtil;

    @Override
    protected NodeResult doExecute(Workflow workflow, AbsNode node) throws Exception {
        ConditionNodeParams params= parseParams(node, ConditionNodeParams.class);
        ConditionNodeParams.Branch branch = executeBranch(workflow, params.getBranch());
        assert branch != null;
        return new NodeResult(Map.of("branchId", branch.getId(), "branchName", branch.getType()));
    }

    private ConditionNodeParams.Branch executeBranch(Workflow workflow, List<ConditionNodeParams.Branch> branchList) {
        for (ConditionNodeParams.Branch branch : branchList) {
            if (conditionUtil.assertion(workflow, branch.getCondition(), branch.getConditions())) {
                return branch;
            }
        }
        return null;
    }

}
