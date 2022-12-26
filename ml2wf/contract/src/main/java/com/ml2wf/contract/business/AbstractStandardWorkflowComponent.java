package com.ml2wf.contract.business;


import com.ml2wf.contract.storage.graph.converter.IGraphConstraintsConverter;
import com.ml2wf.contract.storage.graph.dto.GraphConstraintOperand;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.contract.storage.graph.repository.ConstraintsRepository;
import com.ml2wf.core.workflow.StandardWorkflow;

public abstract class AbstractStandardWorkflowComponent<T extends GraphStandardKnowledgeTask<V>,
        C extends GraphConstraintOperand<V>, V extends GraphTaskVersion> implements IStandardWorkflowComponent {

    private static final String ROOT_CONSTRAINT_NODE_NAME = "__ROOT_CONSTRAINT";

    private final IStandardKnowledgeComponent standardKnowledgeComponent;
    private final ConstraintsRepository<C, V, ?> constraintsRepository;
    private final IGraphConstraintsConverter<T, V> constraintsConverter;

    protected AbstractStandardWorkflowComponent(IStandardKnowledgeComponent standardKnowledgeComponent,
                                                ConstraintsRepository<C, V, ?> constraintsRepository,
                                                IGraphConstraintsConverter<T, V> constraintsConverter) {
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
