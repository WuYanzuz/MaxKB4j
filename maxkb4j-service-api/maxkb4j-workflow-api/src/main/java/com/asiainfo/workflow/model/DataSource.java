package com.asiainfo.workflow.model;

import com.asiainfo.common.domain.dto.OssFile;
import lombok.Data;

import java.util.List;

@Data
public class DataSource {
    private String nodeId;
    private String sourceUrl;
    private String selector;
    private List<OssFile> fileList;
}