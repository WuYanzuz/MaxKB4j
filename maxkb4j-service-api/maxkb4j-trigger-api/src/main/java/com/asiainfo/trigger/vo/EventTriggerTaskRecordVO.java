package com.asiainfo.trigger.vo;

import com.asiainfo.trigger.entity.EventTriggerTaskRecordEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EventTriggerTaskRecordVO extends EventTriggerTaskRecordEntity {
    private String sourceName;
}
