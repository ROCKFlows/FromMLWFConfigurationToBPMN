package com.ml2wf.v3.app.business.components;

import com.ml2wf.v3.app.business.storage.graph.ConstraintsRepository;
import com.ml2wf.v3.app.business.storage.graph.IArangoConstraintsConverter;
import com.ml2wf.v3.workflow.StandardWorkflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StandardWorkflowComponent {

    private static final String ROOT_CONSTRAINT_NODE_NAME = "__ROOT_CONSTRAINT";

    private final StandardKnowledgeComponent standardKnowledgeComponent;
    private final ConstraintsRepository constraintsRepository;
    private final IArangoConstraintsConverter constraintsConverter;

    public StandardWorkflowComponent(@Autowired StandardKnowledgeComponent standardKnowledgeComponent,
                                     @Autowired ConstraintsRepository constraintsRepository,
                                     @Autowired IArangoConstraintsConverter constraintsConverter) {
        this.standardKnowledgeComponent = standardKnowledgeComponent;
        this.constraintsRepository = constraintsRepository;
        this.constraintsConverter = constraintsConverter;
    }

    public boolean importStandardWorkflow(String versionName, StandardWorkflow standardWorkflow) {
        var standardKnowledgeTree = standardKnowledgeComponent.getStandardKnowledgeTree(versionName);
        // TODO: import + merge
        return true;
    }

    public boolean isStandardWorkflowConsistent(String versionName, StandardWorkflow standardWorkflow) {
        var operands = constraintsRepository.findAllByTypeEqualsAndVersion_Name(
                ROOT_CONSTRAINT_NODE_NAME, versionName).get(0).getOperands();
        return operands.stream()
                .map(constraintsConverter::toConstraintTree)
                .peek(t -> {
                    System.out.println("t = " + t);
                    System.out.println(t.isWorkflowConsistent(standardWorkflow));
                })
                .allMatch(c -> c.isWorkflowConsistent(standardWorkflow));
    }
}
