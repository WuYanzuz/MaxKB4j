package com.asiainfo.knowledge.dto;

import com.asiainfo.knowledge.entity.ProblemEntity;
import lombok.Data;

import java.util.List;

@Data
public class ParagraphAddDTO  {
    private String title;
    private String content;
    private Integer position;
    private List<ProblemEntity> problemList;


}
