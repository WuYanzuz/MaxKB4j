package com.asiainfo.trigger.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.asiainfo.trigger.dto.EventTaskQuery;
import com.asiainfo.trigger.entity.EventTriggerTaskRecordEntity;
import com.asiainfo.trigger.vo.EventTriggerTaskRecordVO;

public interface IEventTriggerTaskRecordService extends IService<EventTriggerTaskRecordEntity> {
    IPage<EventTriggerTaskRecordVO> pageList(String id, int current, int size, EventTaskQuery query);

    EventTriggerTaskRecordEntity get(String id, String taskId, String recordId);
}
