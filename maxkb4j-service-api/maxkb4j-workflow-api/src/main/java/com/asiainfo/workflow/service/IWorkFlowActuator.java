package com.asiainfo.workflow.service;

import com.asiainfo.workflow.model.Workflow;

public interface IWorkFlowActuator {
    void execute(Workflow workflow);
}
