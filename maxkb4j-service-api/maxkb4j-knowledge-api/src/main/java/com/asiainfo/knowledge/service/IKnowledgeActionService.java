package com.asiainfo.knowledge.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.asiainfo.knowledge.entity.KnowledgeActionEntity;

public interface IKnowledgeActionService extends IService<KnowledgeActionEntity> {

    void updateState(String id, JSONObject details, String state);
}
