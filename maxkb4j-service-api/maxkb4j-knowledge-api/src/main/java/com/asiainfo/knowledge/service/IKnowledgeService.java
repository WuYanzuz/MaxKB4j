package com.asiainfo.knowledge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.asiainfo.knowledge.entity.KnowledgeEntity;

import java.util.List;

public interface IKnowledgeService extends IService<KnowledgeEntity> {

    List<KnowledgeEntity> listNameAndDescByIds(List<String> knowledgeIds);
}
