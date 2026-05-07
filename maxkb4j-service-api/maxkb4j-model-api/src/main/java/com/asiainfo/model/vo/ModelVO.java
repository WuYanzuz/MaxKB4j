package com.asiainfo.model.vo;

import com.asiainfo.model.entity.ModelEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ModelVO extends ModelEntity {
    private String nickname;
}
