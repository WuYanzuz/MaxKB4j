package com.asiainfo.application.dto;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.application.entity.ApplicationEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApplicationDTO extends ApplicationEntity {

    private JSONObject  workFlowTemplate;
}
