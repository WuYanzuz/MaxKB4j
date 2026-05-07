package com.asiainfo.workflow.compare.impl;


import com.asiainfo.workflow.annotation.CompareType;
import com.asiainfo.workflow.compare.AbstractLengthCompare;
import com.asiainfo.workflow.enums.CompareOperator;
import org.springframework.stereotype.Component;

@Component
@CompareType(CompareOperator.LENGTH_GE)
public class LengthGECompare extends AbstractLengthCompare {

    @Override
    protected boolean compareLength(int sourceLen, int targetLen) {
        return sourceLen >= targetLen;
    }
}