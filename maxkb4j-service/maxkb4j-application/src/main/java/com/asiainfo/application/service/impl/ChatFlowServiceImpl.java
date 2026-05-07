package com.asiainfo.application.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.application.service.IChatService;
import com.asiainfo.application.vo.ApplicationVO;
import com.asiainfo.common.domain.dto.Answer;
import com.asiainfo.common.domain.dto.ChatMessageVO;
import com.asiainfo.common.domain.dto.ChatParams;
import com.asiainfo.common.domain.dto.ChatResponse;
import com.asiainfo.workflow.builder.NodeBuilder;
import com.asiainfo.workflow.enums.WorkflowMode;
import com.asiainfo.workflow.logic.LogicFlow;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;
import com.asiainfo.workflow.service.IWorkFlowActuator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class ChatFlowServiceImpl implements IChatService {

    private final IWorkFlowActuator workFlowActuator;
    private final NodeBuilder nodeBuilder;

    @Override
    public ChatResponse chatMessage(ApplicationVO application, ChatParams chatParams, Sinks.Many<ChatMessageVO> sink) {
       // chatParams.setChatRecordId(chatParams.getChatRecordId() == null ? IdWorker.get32UUID() : chatParams.getChatRecordId());
        LogicFlow logicFlow = LogicFlow.newInstance(application.getWorkFlow());
        List<AbsNode> nodes = logicFlow.getNodes().stream().map(nodeBuilder::getNode).filter(Objects::nonNull).toList();
        Workflow workflow = Workflow.builder(WorkflowMode.APPLICATION, nodes, logicFlow.getEdges())
                .chatParams(chatParams)
                .sink(sink)
                .build();
        workFlowActuator.execute(workflow);
        List<Answer> answerTextList = workflow.output().answers();
        JSONObject details = workflow.output().runtimeDetails();
        return new ChatResponse(answerTextList, details);
    }

}