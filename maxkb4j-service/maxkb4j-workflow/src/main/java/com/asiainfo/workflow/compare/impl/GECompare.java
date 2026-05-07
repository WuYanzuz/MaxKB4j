package com.asiainfo.workflow.compare.impl;


import com.asiainfo.workflow.annotation.CompareType;
import com.asiainfo.workflow.compare.AbstractNumericCompare;
import com.asiainfo.workflow.enums.CompareOperator;
import org.springframework.stereotype.Component;

@Component
@CompareType(CompareOperator.GE)
public class GECompare extends AbstractNumericCompare {

    @Override
    protected boolean compareNumeric(double source, double target) {
        return source >= target;
    }
}