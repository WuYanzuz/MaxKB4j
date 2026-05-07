package com.asiainfo.workflow.handler.node.impl;

import com.asiainfo.core.assistant.NL2SqlAssistant;
import com.asiainfo.core.langchain4j.AppChatMemory;
import com.asiainfo.core.langchain4j.AssistantServices;
import com.asiainfo.core.util.DatabaseUtil;
import com.asiainfo.model.service.IModelProviderService;
import com.asiainfo.workflow.annotation.NodeHandlerType;
import com.asiainfo.workflow.enums.NodeType;
import com.asiainfo.workflow.handler.node.AbsNodeHandler;
import com.asiainfo.workflow.model.NodeResult;
import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.workflow.node.AbsNode;
import com.asiainfo.workflow.node.impl.NL2SqlNode;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.service.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Slf4j
@NodeHandlerType(NodeType.NL2SQL)
@RequiredArgsConstructor
@Component
public class NL2SqlNodeHandler extends AbsNodeHandler {

    private final IModelProviderService modelFactory;

    @Override
    protected NodeResult doExecute(Workflow workflow, AbsNode node) throws Exception {
        NL2SqlNode.NodeParams params = parseParams(node, NL2SqlNode.NodeParams.class);
        NL2SqlNode.DatabaseSetting databaseSetting = params.getDatabaseSetting();
        List<String> fields = params.getQuestionReferenceAddress();
        String question = getReferenceFieldAsString(workflow, fields);

        ChatModel chatModel = modelFactory.buildChatModel(params.getModelId(), params.getModelParamsSetting());
        DataSource dataSource = DatabaseUtil.getDataSource(
                databaseSetting.getType(), databaseSetting.getHost(), databaseSetting.getPort(),
                databaseSetting.getUsername(), databaseSetting.getPassword(), databaseSetting.getDatabase());

        String sqlDialect = DatabaseUtil.getSqlDialect(dataSource);
        String databaseStructure = DatabaseUtil.generateDDL(dataSource);
        List<ChatMessage> historyMessages = workflow.getHistoryMessages(params.getDialogueNumber(), params.getDialogueType(), node.getRuntimeNodeId());

        NL2SqlAssistant assistant = AssistantServices.builder(NL2SqlAssistant.class)
                .chatModel(chatModel)
                .chatMemory(AppChatMemory.withMessages(historyMessages))
                .build();

        Result<String> result = assistant.generateSqlQuery(sqlDialect, databaseStructure, question);
        String sql = DatabaseUtil.cleanSql(result.content());
        String sqlResult = DatabaseUtil.executeSqlQuery(result.content(), dataSource);

        TokenUsage tokenUsage = result.tokenUsage();
        putDetails(node, Map.of(
                "question", question,
                "messageTokens", tokenUsage.inputTokenCount(),
                "answerTokens", tokenUsage.outputTokenCount()
        ));

        return new NodeResult(Map.of("sql", sql, "result", sqlResult));
    }
}
