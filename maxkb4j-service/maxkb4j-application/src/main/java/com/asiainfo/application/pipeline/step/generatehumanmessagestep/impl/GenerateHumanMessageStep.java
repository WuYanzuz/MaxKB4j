package com.asiainfo.application.pipeline.step.generatehumanmessagestep.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.asiainfo.application.pipeline.step.generatehumanmessagestep.AbsGenerateHumanMessageStep;
import com.asiainfo.common.mp.entity.KnowledgeSetting;
import com.asiainfo.common.mp.entity.LlmModelSetting;
import com.asiainfo.knowledge.util.RagContentInjector;
import com.asiainfo.knowledge.vo.ParagraphVO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenerateHumanMessageStep extends AbsGenerateHumanMessageStep {

    public static final RagContentInjector contentInjector = new RagContentInjector();
    @Override
    protected String execute(LlmModelSetting llmModelSetting, KnowledgeSetting knowledgeSetting, String problemText, List<ParagraphVO> paragraphList) {
        String safeProblemText = problemText != null ? problemText : "";
        int maxCharNumber = knowledgeSetting.getMaxParagraphCharNumber();
        if (!CollectionUtils.isEmpty(paragraphList)) {
           return contentInjector.inject(paragraphList, safeProblemText,maxCharNumber);
        }
        return safeProblemText;
    }

    @Override
    public JSONObject getDetails() {
        JSONObject details = new JSONObject(true);
        details.put("step_type", "generate_human_message");
        details.put("messageTokens", context.getOrDefault("messageTokens", 0));
        details.put("answerTokens", context.getOrDefault("answerTokens", 0));
        return details;
    }

}
