package com.asiainfo.workflow.handler.node.impl;

import com.asiainfo.common.domain.dto.MessageConverter;
import com.asiainfo.core.assistant.Assistant;
import com.asiainfo.core.langchain4j.AppChatMemory;
import com.asiainfo.core.langchain4j.AssistantServices;
import com.asiainfo.model.service.IModelProviderService;
import com.asiainfo.workflow.annotation.NodeHandlerType;
import com.asiainfo.workflow.enums.DialogueType;
import com.asiainfo.workflow.enums.NodeType;
import com.asiainfo.workflow.handler.node.AbsNodeHandler;
import com.asiainfo.workflow.model.NodeResult;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;
import com.asiainfo.workflow.node.impl.QuestionNode;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@NodeHandlerType(NodeType.QUESTION)
@RequiredArgsConstructor
@Component
public class QuestionNodeHandler extends AbsNodeHandler {

    private final IModelProviderService modelFactory;

    @Override
    protected NodeResult doExecute(Workflow workflow, AbsNode node) throws Exception {
        QuestionNode.NodeParams params = parseParams(node, QuestionNode.NodeParams.class);
        ChatModel chatModel = modelFactory.buildChatModel(params.getModelId(), params.getModelParamsSetting());
        List<ChatMessage> historyMessages = workflow.getHistoryMessages(params.getDialogueNumber(), DialogueType.WORK_FLOW.name(), node.getRuntimeNodeId());

        putDetail(node, "history_message", MessageConverter.resetMessageList(historyMessages));

        String question = workflow.renderPrompt(params.getPrompt());
        String systemPrompt = workflow.renderPrompt(params.getSystem());

        Assistant assistant = AssistantServices.builder(Assistant.class)
                .systemMessage(systemPrompt)
                .chatMemory(AppChatMemory.withMessages(historyMessages))
                .chatModel(chatModel)
                .build();

        Result<String> result = assistant.chat(question);

        // 使用辅助方法批量写入详情
        putDetails(node, Map.of(
                "system", systemPrompt,
                "question", question
        ));

        TokenUsage tokenUsage = result.tokenUsage();
        if (tokenUsage != null) {
            putDetails(node, Map.of(
                    "messageTokens", tokenUsage.inputTokenCount(),
                    "answerTokens", tokenUsage.outputTokenCount()
            ));
        }

        if (params.getIsResult()) {
            setAnswer(node, result.content());
        }

        return new NodeResult(Map.of("answer", result.content()));
    }
}
