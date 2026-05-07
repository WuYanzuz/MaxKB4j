package com.asiainfo.trigger.vo;

import com.asiainfo.trigger.entity.EventTriggerTaskEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EventTriggerTaskVO extends EventTriggerTaskEntity {
    private String type;
    private String icon;
    private String name;
}
