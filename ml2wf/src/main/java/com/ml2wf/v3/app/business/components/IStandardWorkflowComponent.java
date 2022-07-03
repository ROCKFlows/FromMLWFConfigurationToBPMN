package com.ml2wf.v3.app.business.components;

import com.ml2wf.v3.app.workflow.StandardWorkflow;
import org.springframework.stereotype.Component;

@Component
public interface IStandardWorkflowComponent {

    boolean importStandardWorkflow(String versionName, StandardWorkflow standardWorkflow);

    boolean isStandardWorkflowConsistent(String versionName, StandardWorkflow standardWorkflow);
}
