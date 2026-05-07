package com.asiainfo.application.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.asiainfo.application.entity.ApplicationApiKeyEntity;

public interface IApplicationApiKeyService extends IService<ApplicationApiKeyEntity> {
    ApplicationApiKeyEntity getBySecretKey(String secretKey);
}
