package com.asiainfo.application.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.asiainfo.application.entity.ApplicationVersionEntity;
import com.asiainfo.application.mapper.ApplicationVersionMapper;
import com.asiainfo.application.vo.ApplicationVO;
import com.asiainfo.common.util.BeanUtil;
import org.springframework.stereotype.Service;

/**
 * @author tarzan
 * @date 2024-12-28 18:47:27
 */
@Service
public class ApplicationVersionService extends ServiceImpl<ApplicationVersionMapper, ApplicationVersionEntity>{

    public ApplicationVO getAppLatestOne(String appId) {
        LambdaQueryWrapper<ApplicationVersionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApplicationVersionEntity::getApplicationId, appId);
        wrapper.last("limit 1");
        wrapper.orderByDesc(ApplicationVersionEntity::getCreateTime);
        ApplicationVersionEntity entity = this.getOne(wrapper);
        if (entity == null) {
            return null;
        }
        ApplicationVO vo = BeanUtil.copy(entity, ApplicationVO.class);
        vo.setId(entity.getApplicationId());
        vo.setName(entity.getApplicationName());
        return vo;
    }
}
