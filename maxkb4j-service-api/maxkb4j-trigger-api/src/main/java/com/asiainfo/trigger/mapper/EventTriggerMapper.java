package com.asiainfo.trigger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.asiainfo.trigger.dto.EventQuery;
import com.asiainfo.trigger.entity.EventTriggerEntity;
import com.asiainfo.trigger.vo.EventTriggerVO;
import org.apache.ibatis.annotations.Param;

public interface EventTriggerMapper extends BaseMapper<EventTriggerEntity> {

    IPage<EventTriggerVO> selectEventTriggerPage(Page<EventTriggerVO> page, @Param("query") EventQuery query);
}
