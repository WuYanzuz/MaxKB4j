package com.asiainfo.trigger.vo;

import com.asiainfo.application.entity.ApplicationEntity;
import com.asiainfo.tool.entity.ToolEntity;
import com.asiainfo.trigger.entity.EventTriggerEntity;
import com.asiainfo.trigger.entity.EventTriggerTaskEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SourceEventTriggerVO extends EventTriggerEntity {
    private EventTriggerTaskEntity triggerTask;
    private String createUser;
    private String nextRunTime;
    private String triggerTaskStr;
    private ApplicationEntity applicationTask;
    private ToolEntity toolTask;
}
