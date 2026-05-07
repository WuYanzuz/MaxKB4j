package com.asiainfo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.asiainfo.system.entity.ResourceMappingEntity;
import com.asiainfo.system.entity.TargetResource;
import com.asiainfo.system.mapper.ResourceMappingMapper;
import com.asiainfo.system.service.IResourceMappingService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ResourceMappingServiceImpl extends ServiceImpl<ResourceMappingMapper, ResourceMappingEntity> implements IResourceMappingService {


    @Transactional
    @Override
    public boolean relation(String sourceType, String sourceId, List<TargetResource> targets) {
        this.remove(Wrappers.<ResourceMappingEntity>lambdaQuery().eq(ResourceMappingEntity::getSourceType, sourceType).eq(ResourceMappingEntity::getSourceId, sourceId));
        List<ResourceMappingEntity> list = targets.stream().map(target -> {
            ResourceMappingEntity entity = new ResourceMappingEntity();
            entity.setTargetId(target.getTargetId());
            entity.setSourceType(sourceType);
            entity.setSourceId(sourceId);
            entity.setTargetType(target.getTargetType());
            return entity;
        }).toList();
        return this.saveBatch( list);
    }

    @Override
    public boolean deleteBySourceId(String sourceType, String sourceId) {
        LambdaQueryWrapper<ResourceMappingEntity> wrapper = Wrappers.<ResourceMappingEntity>lambdaQuery()
                .eq(StringUtils.isNotBlank(sourceType),ResourceMappingEntity::getSourceType, sourceType)
                .eq(StringUtils.isNotBlank(sourceId),ResourceMappingEntity::getSourceId, sourceId);
        return this.remove(wrapper);
    }

}