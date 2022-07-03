package com.ml2wf.v3.app.business.components;

import com.ml2wf.v3.app.workflow.converter.BPMNWorkflowConverter;
import com.ml2wf.v3.app.workflow.custom.bpmn.BPMNWorkflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class BPMNComponent {

    // TODO: intercept this method return and convert automatically standard workflow to bpmn (Aspect4J ?)
/*
    private final StandardWorkflowComponent standardWorkflowComponent;
    private final BPMNWorkflowConverter bpmnWorkflowConverter; // TODO: make autowirable

    public BPMNComponent(@Autowired StandardWorkflowComponent standardWorkflowComponent) {
        this.standardWorkflowComponent = standardWorkflowComponent;
        bpmnWorkflowConverter = new BPMNWorkflowConverter();
    }

    public boolean importWorkflow(String versionName, BPMNWorkflow bpmnWorkflow) {
        var standardWorkflow = bpmnWorkflowConverter.toStandardWorkflow(bpmnWorkflow);
        return standardWorkflowComponent.importStandardWorkflow(versionName, standardWorkflow);
    }

    public boolean isBPMNWorkflowConsistent(String versionName, BPMNWorkflow bpmnWorkflow) {
        var standardWorkflow = bpmnWorkflowConverter.toStandardWorkflow(bpmnWorkflow);
        return standardWorkflowComponent.isStandardWorkflowConsistent(versionName, standardWorkflow);
    }*/
}
