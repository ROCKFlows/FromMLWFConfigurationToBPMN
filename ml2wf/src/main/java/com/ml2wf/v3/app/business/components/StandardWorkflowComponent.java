package com.ml2wf.v3.app.business.components;

import com.ml2wf.v3.app.business.storage.graph.contracts.converter.IGraphConstraintsConverter;
import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphConstraintOperand;
import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphTaskVersion;
import com.ml2wf.v3.app.business.storage.graph.contracts.repository.ConstraintsRepository;
import com.ml2wf.v3.app.workflow.StandardWorkflow;

public abstract class StandardWorkflowComponent<T extends GraphStandardKnowledgeTask<T, V>,
        C extends GraphConstraintOperand<T, V, C>, V extends GraphTaskVersion, ID> implements IStandardWorkflowComponent {

    private static final String ROOT_CONSTRAINT_NODE_NAME = "__ROOT_CONSTRAINT";

    private final IStandardKnowledgeComponent standardKnowledgeComponent;
    private final ConstraintsRepository<C, T, V, ID> constraintsRepository;
    private final IGraphConstraintsConverter<T, C, V> constraintsConverter;

    protected StandardWorkflowComponent(IStandardKnowledgeComponent standardKnowledgeComponent,
                                        ConstraintsRepository<C, T, V, ID> constraintsRepository,
                                        IGraphConstraintsConverter<T, C, V> constraintsConverter) {
        this.standardKnowledgeComponent = standardKnowledgeComponent;
        this.constraintsRepository = constraintsRepository;
        this.constraintsConverter = constraintsConverter;
    }

    @Override
    public boolean importStandardWorkflow(String versionName, StandardWorkflow standardWorkflow) {
        var standardKnowledgeTree = standardKnowledgeComponent.getStandardKnowledgeTree(versionName);
        // TODO: import + merge
        return true;
    }

    @Override
    public boolean isStandardWorkflowConsistent(String versionName, StandardWorkflow standardWorkflow) {
        var operands = constraintsRepository.findAllByTypeEqualsAndVersion_Name(
                ROOT_CONSTRAINT_NODE_NAME, versionName).get(0).getOperands();
        return operands.stream()
                .map(constraintsConverter::toConstraintTree)
                .allMatch(c -> c.isWorkflowConsistent(standardWorkflow));
    }
}
