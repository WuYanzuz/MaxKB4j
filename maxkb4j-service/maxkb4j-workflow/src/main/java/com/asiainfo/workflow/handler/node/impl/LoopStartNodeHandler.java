package com.asiainfo.workflow.handler.node.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.workflow.annotation.NodeHandlerType;
import com.asiainfo.workflow.enums.NodeType;
import com.asiainfo.workflow.handler.node.AbsNodeHandler;
import com.asiainfo.workflow.model.LoopParams;
import com.asiainfo.workflow.model.LoopWorkFlow;
import com.asiainfo.workflow.model.NodeResult;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;
import org.springframework.stereotype.Component;

import java.util.Map;

@NodeHandlerType(NodeType.LOOP_START)
@Component
public class LoopStartNodeHandler extends AbsNodeHandler {

    @Override
    protected NodeResult doExecute(Workflow workflow, AbsNode node) throws Exception {
        int index = 0;
        Object item = "None";
        if (workflow instanceof LoopWorkFlow loopWorkFlow) {
            LoopParams loopParams = loopWorkFlow.getLoopParams();
            index = loopParams.getIndex();
            item = loopParams.getItem();
            JSONArray loopInputFieldList = node.getProperties().getJSONArray("loopInputFieldList");
            if (loopInputFieldList != null) {
                for (int i = 0; i < loopInputFieldList.size(); i++) {
                    JSONObject loopInputField = loopInputFieldList.getJSONObject(i);
                    String key = loopInputField.getString("field");
                    loopWorkFlow.getLoopContext().put(key, "");
                }
            }
        }
        return new NodeResult(Map.of("index", index, "item", item));
    }
}
