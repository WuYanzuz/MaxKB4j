package com.asiainfo.workflow.annotation;


import com.asiainfo.workflow.enums.CompareOperator;

import java.lang.annotation.*;

/**
 * Annotation to declare the comparison types supported by a Compare implementation.
 * Used by CompareAutoRegistrar for automatic handler registration.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CompareType {
    /**
     * The comparison operators supported by this handler
     */
    CompareOperator[] value();
}