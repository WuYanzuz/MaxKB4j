package com.asiainfo.trigger.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.asiainfo.trigger.dto.EventQuery;
import com.asiainfo.trigger.dto.EventTriggerDTO;
import com.asiainfo.trigger.entity.EventTriggerEntity;
import com.asiainfo.trigger.vo.EventTriggerVO;
import com.asiainfo.trigger.vo.SourceEventTriggerVO;

import java.util.List;

public interface IEventTriggerService extends IService<EventTriggerEntity> {
    IPage<EventTriggerVO> pageList(int current, int size, EventQuery query);

    void saveTrigger(EventTriggerDTO dto, Boolean isEdit);

    boolean batchActivate(String id, Boolean isActive);

    boolean deleteTrigger(String id);

    EventTriggerVO getDetailById(String id);

    List<EventTriggerEntity> listBySource(String sourceType, String sourceId);

    SourceEventTriggerVO getDetailBySourceId(String id,String sourceType,String sourceId);

}
