package com.asiainfo.application.dto;

import com.asiainfo.application.entity.ApplicationEntity;
import com.asiainfo.tool.entity.ToolEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaxKb4J {
    private ApplicationEntity application;
    private List<ToolEntity> toolList;
    private String version;
}
