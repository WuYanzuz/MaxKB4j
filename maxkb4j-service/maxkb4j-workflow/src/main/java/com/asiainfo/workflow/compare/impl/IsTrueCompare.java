package com.asiainfo.workflow.compare.impl;


import com.asiainfo.workflow.annotation.CompareType;
import com.asiainfo.workflow.compare.Compare;
import com.asiainfo.workflow.enums.CompareOperator;
import org.springframework.stereotype.Component;

@Component
@CompareType(CompareOperator.IS_TRUE)
public class IsTrueCompare implements Compare {

    @Override
    public boolean compare(Object sourceValue, Object targetValue) {
        if (sourceValue == null) {
            return false;
        }
        if (sourceValue instanceof Boolean) {
            return (Boolean) sourceValue;
        } else if (sourceValue instanceof String) {
            return Boolean.parseBoolean((String) sourceValue);
        } else {
            return false;
        }
    }
}