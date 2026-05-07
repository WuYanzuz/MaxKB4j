package com.asiainfo.workflow.compare.impl;


import com.asiainfo.workflow.annotation.CompareType;
import com.asiainfo.workflow.compare.Compare;
import com.asiainfo.workflow.enums.CompareOperator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Objects;

@Component
@CompareType(CompareOperator.IS_NOT_NULL)
public class IsNotNullCompare implements Compare {

    @Override
    public boolean compare(Object sourceValue, Object targetValue) {
        if (sourceValue instanceof Collection) {
            return !CollectionUtils.isEmpty((Collection<?>) sourceValue);
        } else {
            return Objects.nonNull(sourceValue) && !"".equals(sourceValue);
        }
    }
}