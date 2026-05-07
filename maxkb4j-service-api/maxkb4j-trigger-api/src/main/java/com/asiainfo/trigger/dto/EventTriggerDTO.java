package com.asiainfo.trigger.dto;

import com.asiainfo.trigger.entity.EventTriggerEntity;
import com.asiainfo.trigger.vo.EventTriggerTaskVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class EventTriggerDTO extends EventTriggerEntity {
    private List<EventTriggerTaskVO> triggerTask;
    private List<String> idList;
}
