package com.asiainfo.application.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.asiainfo.common.mp.base.BaseEntity;
import com.asiainfo.common.typehandler.JSONBTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "application_access", autoResultMap = true)
public class ApplicationAccessEntity extends BaseEntity {
    @TableField(typeHandler = JSONBTypeHandler.class)
    private JSONObject status;
    @TableField(typeHandler = JSONBTypeHandler.class)
    private JSONObject config;
}
