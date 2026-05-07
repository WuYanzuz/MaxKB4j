package com.asiainfo.knowledge.vo;

import lombok.Data;

@Data
public class KnowledgeListVO {
    private String id;
    private String name;
    private String desc;
    private Integer type;
    private String folderId;
    private String resourceType="knowledge";
}
