package com.asiainfo.application.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.asiainfo.common.mp.base.BaseEntity;
import com.asiainfo.common.typehandler.JSONBTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tarzan
 * @date 2024-12-26 09:50:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "application_chat",autoResultMap = true)
public class ApplicationChatEntity extends BaseEntity {
    private String summary;
    private String applicationId;
    private String chatUserId;
    private String chatUserType;
    @TableField(typeHandler = JSONBTypeHandler.class)
    private JSONObject asker;
    @TableField(typeHandler = JSONBTypeHandler.class)
    private JSONObject meta;
    private Integer starNum;
    private Integer trampleNum;
    private Integer chatRecordCount;
    private Integer markSum;
    private Boolean isDeleted;
    private String ipAddress;
    @TableField(typeHandler = JSONBTypeHandler.class)
    private JSONObject source;
} 
