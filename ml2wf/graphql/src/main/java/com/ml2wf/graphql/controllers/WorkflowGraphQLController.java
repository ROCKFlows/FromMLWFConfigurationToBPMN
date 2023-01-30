package com.ml2wf.graphql.controllers;

import com.ml2wf.contract.business.IStandardWorkflowComponent;
import com.ml2wf.core.workflow.StandardWorkflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WorkflowGraphQLController {

    private final IStandardWorkflowComponent workflowComponent;

    WorkflowGraphQLController(@Autowired IStandardWorkflowComponent workflowComponent) {
        this.workflowComponent = workflowComponent;
    }

    @QueryMapping
    StandardWorkflow workflow(@Argument String versionName) {
        return workflowComponent.getStandardWorkflow(versionName);
    }
}
