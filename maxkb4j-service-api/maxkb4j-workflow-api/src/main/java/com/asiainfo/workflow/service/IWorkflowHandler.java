package com.asiainfo.workflow.service;

import com.asiainfo.workflow.model.Workflow;

public interface IWorkflowHandler {
    /**
     * Check if this handler can handle the given workflow.
     *
     * @param workflow the workflow to check
     * @return true if this handler can handle the workflow
     */
    default boolean canHandle(Workflow workflow) {
        return true;
    }

    /**
     * Execute the workflow.
     *
     * @param workflow the workflow to execute
     */
    void execute(Workflow workflow);
}



