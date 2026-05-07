package com.asiainfo.application.pipeline.step.searchdatasetstep;


import com.asiainfo.application.vo.ApplicationVO;
import com.asiainfo.common.mp.entity.KnowledgeSetting;
import com.asiainfo.application.pipeline.AbsStep;
import com.asiainfo.application.pipeline.PipelineManage;
import com.asiainfo.knowledge.vo.ParagraphVO;

import java.util.List;

public abstract class AbsSearchDatasetStep extends AbsStep {
    @Override
    protected void _run(PipelineManage manage) {
        ApplicationVO application = manage.application;
        String problemText = manage.chatParams.getMessage();
        String paddingProblemText = (String) manage.context.get("paddingProblemText");
        Boolean reChat =  manage.chatParams.getReChat();
        List<String> knowledgeIds= application.getKnowledgeIds();
        KnowledgeSetting datasetSetting = application.getKnowledgeSetting();
        List<ParagraphVO> paragraphList = execute(knowledgeIds,datasetSetting, problemText, paddingProblemText, reChat,manage);
        manage.context.put("paragraphList", paragraphList);
    }

    protected abstract List<ParagraphVO> execute(List<String> knowledgeIds, KnowledgeSetting datasetSetting, String problemText, String paddingProblemText, Boolean reChat, PipelineManage manage);
}
