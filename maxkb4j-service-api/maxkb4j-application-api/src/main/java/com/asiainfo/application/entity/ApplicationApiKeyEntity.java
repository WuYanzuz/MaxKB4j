package com.asiainfo.application.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.asiainfo.common.mp.base.BaseEntity;
import com.asiainfo.common.typehandler.StringListTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
  * @author tarzan
  * @date 2025-01-02 09:01:12
  */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "application_api_key", autoResultMap = true)
public class ApplicationApiKeyEntity extends BaseEntity {
	private String secretKey;
	private Boolean isActive;
	private String applicationId;
	private String userId;
	private Boolean allowCrossDomain;
	@TableField(typeHandler = StringListTypeHandler.class)
	private List<String> crossDomainList;
} 
