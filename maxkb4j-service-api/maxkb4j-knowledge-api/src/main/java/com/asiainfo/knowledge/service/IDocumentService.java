package com.asiainfo.knowledge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.asiainfo.knowledge.dto.DocumentSimple;
import com.asiainfo.knowledge.entity.DocumentEntity;

import java.util.List;

public interface IDocumentService extends IService<DocumentEntity> {

    void updateStatusById(String id, int type, int status);
    void updateStatusByIds(List<String> ids, int type, int status);
    void updateStatusMetaById(String id);
    boolean batchCreateDocs(String knowledgeId,int knowledgeType, List<DocumentSimple> docs);

}
