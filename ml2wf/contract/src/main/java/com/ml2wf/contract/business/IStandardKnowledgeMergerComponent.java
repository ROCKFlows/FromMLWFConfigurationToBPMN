package com.ml2wf.contract.business;

import com.ml2wf.core.workflow.StandardWorkflow;
import org.springframework.stereotype.Component;

@Component
public interface IStandardKnowledgeMergerComponent {

    void mergeWorkflowWithTree(String version, StandardWorkflow workflow);
}
