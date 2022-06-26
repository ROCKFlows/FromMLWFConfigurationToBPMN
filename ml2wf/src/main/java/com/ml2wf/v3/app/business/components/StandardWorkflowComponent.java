package com.ml2wf.v3.app.business.components;

import com.ml2wf.v3.workflow.StandardWorkflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StandardWorkflowComponent {

    private final StandardKnowledgeComponent standardKnowledgeComponent;

    public StandardWorkflowComponent(@Autowired StandardKnowledgeComponent standardKnowledgeComponent) {
        this.standardKnowledgeComponent = standardKnowledgeComponent;
    }

    public boolean importStandardWorkflow(String versionName, StandardWorkflow standardWorkflow) {
        var standardKnowledgeTree = standardKnowledgeComponent.getStandardKnowledgeTree(versionName);
        // TODO: import + merge
        return true;
    }

    public boolean isStandardWorkflowConsistent(String versionName, StandardWorkflow standardWorkflow) {
        var standardKnowledgeTree = standardKnowledgeComponent.getStandardKnowledgeTree(versionName);
        // TODO: retrieve constraints rather than the whole tree
        return standardKnowledgeTree.getConstraints().stream().allMatch(o -> o.getOperator().isWorkflowConsistent(standardWorkflow));
    }
}
