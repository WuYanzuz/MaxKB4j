package com.asiainfo.trigger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.asiainfo.trigger.dto.EventTaskQuery;
import com.asiainfo.trigger.entity.EventTriggerTaskRecordEntity;
import com.asiainfo.trigger.vo.EventTriggerTaskRecordVO;
import org.apache.ibatis.annotations.Param;

public interface EventTriggerTaskRecordMapper extends BaseMapper<EventTriggerTaskRecordEntity> {

    IPage<EventTriggerTaskRecordVO> pageListWithSourceName(Page<EventTriggerTaskRecordVO> page,
                                                           @Param("triggerId") String triggerId,
                                                           @Param("query") EventTaskQuery query);
}
