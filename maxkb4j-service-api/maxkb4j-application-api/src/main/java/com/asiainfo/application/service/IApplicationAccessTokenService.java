package com.asiainfo.application.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.asiainfo.application.entity.ApplicationAccessTokenEntity;

public interface IApplicationAccessTokenService extends IService<ApplicationAccessTokenEntity> {
    ApplicationAccessTokenEntity getByAccessToken(String accessToken);
}
