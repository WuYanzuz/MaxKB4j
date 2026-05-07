package com.asiainfo.knowledge.dto;

import com.asiainfo.knowledge.entity.KnowledgeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WebKnowledgeDTO extends KnowledgeEntity {
    private String sourceUrl;
    private String selector;
}
