package com.asiainfo.trigger.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.asiainfo.trigger.entity.EventTriggerTaskEntity;
import com.asiainfo.trigger.mapper.EventTriggerTaskMapper;
import org.springframework.stereotype.Service;

@Service
public class EventTriggerTaskService extends ServiceImpl<EventTriggerTaskMapper, EventTriggerTaskEntity> implements IEventTriggerTaskService {
}
