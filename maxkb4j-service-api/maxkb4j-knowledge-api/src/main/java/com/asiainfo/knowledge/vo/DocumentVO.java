package com.asiainfo.knowledge.vo;

import com.asiainfo.knowledge.entity.DocumentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DocumentVO extends DocumentEntity {
    private Integer paragraphCount;
}
