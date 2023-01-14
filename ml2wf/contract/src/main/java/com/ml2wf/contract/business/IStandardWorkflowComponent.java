package com.ml2wf.contract.business;

import com.ml2wf.core.workflow.StandardWorkflow;
import org.springframework.stereotype.Component;

@Component
public interface IStandardWorkflowComponent {

    boolean importStandardWorkflow(String newVersionName, StandardWorkflow standardWorkflow);

    boolean isStandardWorkflowConsistent(String versionName, StandardWorkflow standardWorkflow);
}
