package com.asiainfo.workflow.compare.impl;


import com.asiainfo.workflow.annotation.CompareType;
import com.asiainfo.workflow.compare.AbstractNumericCompare;
import com.asiainfo.workflow.enums.CompareOperator;
import org.springframework.stereotype.Component;

@Component
@CompareType(CompareOperator.LT)
public class LTCompare extends AbstractNumericCompare {

    @Override
    protected boolean compareNumeric(double source, double target) {
        return source < target;
    }
}