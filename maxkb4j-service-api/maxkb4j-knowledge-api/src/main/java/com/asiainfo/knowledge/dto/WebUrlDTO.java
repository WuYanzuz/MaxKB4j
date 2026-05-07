package com.asiainfo.knowledge.dto;

import lombok.Data;

import java.util.List;

@Data
public class WebUrlDTO {
    private String selector;
    private List<String> sourceUrlList;
}
