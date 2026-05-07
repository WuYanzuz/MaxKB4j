package com.asiainfo.application.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.asiainfo.application.dto.ChatQueryDTO;
import com.asiainfo.application.entity.ApplicationChatUserStatsEntity;
import com.asiainfo.application.mapper.ApplicationChatUserStatsMapper;
import com.asiainfo.application.vo.ApplicationStatisticsVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tarzan
 * @date 2024-12-29 10:34:03
 */
@Service
public class ApplicationChatUserStatsService extends ServiceImpl<ApplicationChatUserStatsMapper, ApplicationChatUserStatsEntity>{

    public List<ApplicationStatisticsVO> getCustomerCountTrend(String appId, ChatQueryDTO query) {
        return baseMapper.getCustomerCountTrend(appId,query);
    }

    public ApplicationChatUserStatsEntity getByUserIdAndAppId(String chatUserId, String appId) {
        return this.getOne(Wrappers.<ApplicationChatUserStatsEntity>lambdaQuery().eq(ApplicationChatUserStatsEntity::getChatUserId,chatUserId).eq(ApplicationChatUserStatsEntity::getApplicationId,appId));
    }
}
