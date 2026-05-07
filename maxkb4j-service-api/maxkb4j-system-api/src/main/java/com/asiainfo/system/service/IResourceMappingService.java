package com.asiainfo.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.asiainfo.system.entity.ResourceMappingEntity;
import com.asiainfo.system.entity.TargetResource;

import java.util.List;

public interface IResourceMappingService extends IService<ResourceMappingEntity> {

    boolean relation(String sourceType, String sourceId, List<TargetResource> targets);

    boolean deleteBySourceId(String sourceType,String sourceId);
}
