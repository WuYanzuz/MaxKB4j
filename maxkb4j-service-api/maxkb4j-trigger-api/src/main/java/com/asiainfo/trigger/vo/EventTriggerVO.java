package com.asiainfo.trigger.vo;

import com.asiainfo.application.entity.ApplicationEntity;
import com.asiainfo.tool.entity.ToolEntity;
import com.asiainfo.trigger.dto.EventTriggerDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class EventTriggerVO extends EventTriggerDTO {
    private String createUser;
    private String nextRunTime;
    private List<ApplicationEntity> applicationTaskList;
    private List<ToolEntity> toolTaskList;
}
