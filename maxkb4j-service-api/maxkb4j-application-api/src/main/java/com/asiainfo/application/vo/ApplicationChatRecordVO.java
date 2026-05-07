package com.asiainfo.application.vo;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.common.domain.dto.ParagraphDTO;
import com.asiainfo.application.entity.ApplicationChatRecordEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApplicationChatRecordVO extends ApplicationChatRecordEntity {

    private List<ParagraphDTO> paragraphList;
    private String paddingProblemText;
    private List<JSONObject> executionDetails;

    public void setExecutionDetails(List<JSONObject> executionDetails) {
        this.executionDetails = executionDetails;
        super.setDetails(null);
    }
}
