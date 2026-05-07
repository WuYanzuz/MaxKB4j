package com.asiainfo.knowledge.dto;

import lombok.Data;

import java.util.List;

@Data
public class GenerateProblemDTO {
    private List<String> documentIdList;
    private List<String> paragraphIdList;
    private String modelId;
    private String number;
    private String prompt;
    private List<String> stateList;
}