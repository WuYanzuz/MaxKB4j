package com.asiainfo.trigger.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.asiainfo.trigger.dto.EventTaskQuery;
import com.asiainfo.trigger.entity.EventTriggerTaskRecordEntity;
import com.asiainfo.trigger.mapper.EventTriggerTaskRecordMapper;
import com.asiainfo.trigger.vo.EventTriggerTaskRecordVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EventTriggerTaskRecordService extends ServiceImpl<EventTriggerTaskRecordMapper, EventTriggerTaskRecordEntity> implements IEventTriggerTaskRecordService {

    @Override
    public IPage<EventTriggerTaskRecordVO> pageList(String id, int current, int size, EventTaskQuery query) {
        Page<EventTriggerTaskRecordVO> page = new Page<>(current, size);
        return this.baseMapper.pageListWithSourceName(page, id, query);
    }

    @Override
    public EventTriggerTaskRecordEntity get(String id, String taskId, String recordId) {
        return this.getById(recordId);
    }
}
