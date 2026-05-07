package com.asiainfo.application.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.asiainfo.application.dto.ChatQueryDTO;
import com.asiainfo.application.entity.ApplicationChatEntity;
import com.asiainfo.application.vo.ApplicationStatisticsVO;
import com.asiainfo.application.vo.ChatRecordDetailVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author tarzan
 * @date 2024-12-26 09:50:23
 */
@Mapper
public interface ApplicationChatMapper extends BaseMapper<ApplicationChatEntity>{

    List<ChatRecordDetailVO> chatRecordDetail(List<String>  ids);

    List<ApplicationStatisticsVO> statistics(String appId, ChatQueryDTO query);

    List<ApplicationStatisticsVO> userTokenUsage(String appId, ChatQueryDTO query);

    List<ApplicationStatisticsVO> topQuestions(String appId, ChatQueryDTO query);
}
