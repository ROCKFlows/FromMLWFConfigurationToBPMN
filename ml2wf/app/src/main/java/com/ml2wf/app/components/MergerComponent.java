package com.ml2wf.app.components;

import com.ml2wf.contract.business.IStandardWorkflowComponent;
import com.ml2wf.core.workflow.converter.BPMNWorkflowConverter;
import com.ml2wf.core.workflow.custom.bpmn.BPMNWorkflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MergerComponent {

    // TODO: intercept this method return and convert automatically standard workflow to bpmn (Aspect4J ?)

    private final IStandardWorkflowComponent standardWorkflowComponent;
    private final BPMNWorkflowConverter bpmnWorkflowConverter; // TODO: make autowirable

    public MergerComponent(@Autowired IStandardWorkflowComponent standardWorkflowComponent) {
        this.standardWorkflowComponent = standardWorkflowComponent;
        bpmnWorkflowConverter = new BPMNWorkflowConverter();
    }

    public boolean importWorkflow(String versionName, BPMNWorkflow bpmnWorkflow) {
        var standardWorkflow = bpmnWorkflowConverter.toStandardWorkflow(bpmnWorkflow);
        return standardWorkflowComponent.importStandardWorkflow(versionName, standardWorkflow);
    }
}
