package com.asiainfo.application.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.asiainfo.application.dto.ChatQueryDTO;
import com.asiainfo.application.entity.ApplicationChatUserStatsEntity;
import com.asiainfo.application.vo.ApplicationStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tarzan
 * @date 2024-12-29 10:34:03
 */
@Mapper
public interface ApplicationChatUserStatsMapper extends BaseMapper<ApplicationChatUserStatsEntity>{

    List<ApplicationStatisticsVO> getCustomerCountTrend(String appId, @Param("query") ChatQueryDTO query);

}
