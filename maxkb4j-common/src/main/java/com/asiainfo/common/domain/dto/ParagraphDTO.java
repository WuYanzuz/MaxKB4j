package com.asiainfo.common.domain.dto;

import lombok.Data;

@Data
public class ParagraphDTO {
    private String title;
    private String content;
    private Float similarity;
    private String documentName;
    private String knowledgeName;
    private Integer knowledgeType;
}
