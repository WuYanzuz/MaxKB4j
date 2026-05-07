package com.asiainfo.application.vo;

import com.asiainfo.application.entity.ApplicationEntity;
import com.asiainfo.common.domain.dto.KnowledgeDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApplicationVO extends ApplicationEntity {
    private List<KnowledgeDTO> knowledgeList;
    private String nickname;
    private Boolean showSource;
    private Boolean showExec;
    private String language;
}
