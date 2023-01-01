package com.ml2wf.contract.business;

import com.ml2wf.contract.storage.graph.converter.IGraphConstraintsConverter;
import com.ml2wf.contract.storage.graph.dto.GraphConstraintOperand;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.contract.storage.graph.repository.ConstraintsRepository;
import com.ml2wf.core.workflow.StandardWorkflow;

public abstract class AbstractStandardWorkflowComponent<T extends GraphStandardKnowledgeTask<T, V>,
        V extends GraphTaskVersion, O extends GraphConstraintOperand<O, T, V>> implements IStandardWorkflowComponent {

    private static final String ROOT_CONSTRAINT_NODE_NAME = "__ROOT_CONSTRAINT";

    private final IStandardKnowledgeMergerComponent standardKnowledgeMergerComponent;
    private final ConstraintsRepository<O, T, V, ?> constraintsRepository;
    private final IGraphConstraintsConverter<O, T, V> constraintsConverter;

    protected AbstractStandardWorkflowComponent(IStandardKnowledgeMergerComponent standardKnowledgeMergerComponent,
                                                ConstraintsRepository<O, T, V, ?> constraintsRepository,
                                                IGraphConstraintsConverter<O,T, V> constraintsConverter) {
        this.standardKnowledgeMergerComponent = standardKnowledgeMergerComponent;
        this.constraintsRepository = constraintsRepository;
        this.constraintsConverter = constraintsConverter;
    }

    @Override
    public boolean importStandardWorkflow(String versionName, StandardWorkflow standardWorkflow) {
        standardKnowledgeMergerComponent.mergeWorkflowWithTree(versionName, standardWorkflow);
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
