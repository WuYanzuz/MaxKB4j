package com.asiainfo.application.pipeline.step.generatehumanmessagestep;


import com.asiainfo.common.mp.entity.LlmModelSetting;
import com.asiainfo.application.vo.ApplicationVO;
import com.asiainfo.common.mp.entity.KnowledgeSetting;
import com.asiainfo.application.pipeline.AbsStep;
import com.asiainfo.application.pipeline.PipelineManage;
import com.asiainfo.knowledge.vo.ParagraphVO;

import java.util.List;

public abstract class AbsGenerateHumanMessageStep extends AbsStep {

    @Override
    @SuppressWarnings("unchecked")
    protected void _run(PipelineManage manage) {
        String problemText = manage.chatParams.getMessage();
        List<ParagraphVO> paragraphList = (List<ParagraphVO>) manage.context.get("paragraphList");
        ApplicationVO application= manage.application;
        LlmModelSetting llmModelSetting = application.getModelSetting();
        KnowledgeSetting knowledgeSetting = application.getKnowledgeSetting();
        String prompt = execute(llmModelSetting, knowledgeSetting,problemText, paragraphList);
        manage.context.put("user_prompt", prompt);
    }

    protected abstract String execute(LlmModelSetting llmModelSetting , KnowledgeSetting knowledgeSetting, String problemText, List<ParagraphVO> paragraphList);
}
