package com.asiainfo.workflow.compare.impl;


import com.asiainfo.workflow.annotation.CompareType;
import com.asiainfo.workflow.compare.Compare;
import com.asiainfo.workflow.enums.CompareOperator;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@CompareType(CompareOperator.EQ)
public class EqualCompare implements Compare {

    @Override
    public boolean compare(Object sourceValue, Object targetValue) {
        return Objects.equals(sourceValue, targetValue);
    }
}