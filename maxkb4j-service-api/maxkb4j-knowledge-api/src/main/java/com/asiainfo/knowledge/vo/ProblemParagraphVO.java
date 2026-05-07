package com.asiainfo.knowledge.vo;

import com.asiainfo.knowledge.entity.ProblemParagraphEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProblemParagraphVO extends ProblemParagraphEntity {
    private String content;
}
