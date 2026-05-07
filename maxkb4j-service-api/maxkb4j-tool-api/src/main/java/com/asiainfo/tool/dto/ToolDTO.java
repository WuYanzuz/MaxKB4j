package com.asiainfo.tool.dto;

import com.asiainfo.common.mp.entity.ToolInputField;
import com.asiainfo.tool.entity.ToolEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ToolDTO extends ToolEntity {
    private List<ToolInputField> debugFieldList;
}
