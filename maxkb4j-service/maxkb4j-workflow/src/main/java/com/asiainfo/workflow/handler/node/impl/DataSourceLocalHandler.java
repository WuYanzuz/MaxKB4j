package com.asiainfo.workflow.handler.node.impl;

import com.asiainfo.workflow.model.Workflow;
import com.asiainfo.common.domain.dto.OssFile;
import com.asiainfo.workflow.annotation.NodeHandlerType;
import com.asiainfo.workflow.enums.NodeType;
import com.asiainfo.workflow.handler.node.AbsNodeHandler;
import com.asiainfo.workflow.model.*;
import com.asiainfo.workflow.node.AbsNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@NodeHandlerType(NodeType.DATA_SOURCE_LOCAL)
@RequiredArgsConstructor
public class DataSourceLocalHandler extends AbsNodeHandler {


    @Override
    protected NodeResult doExecute(Workflow workflow, AbsNode node) throws Exception {
        List<OssFile> fileList = new ArrayList<>();
        if (workflow instanceof KnowledgeWorkflow knowledgeWorkflow) {
            KnowledgeParams knowledgeParams = knowledgeWorkflow.getKnowledgeParams();
            DataSource dataSource = knowledgeParams.getDataSource();
            if (dataSource != null) {
                fileList = dataSource.getFileList();
            }
        }
        return new NodeResult(Map.of("fileList", fileList));
    }
}
