package com.ml2wf.contract.business;

import com.ml2wf.contract.storage.graph.converter.IGraphConstraintsConverter;
import com.ml2wf.contract.storage.graph.converter.IGraphStandardWorkflowConverter;
import com.ml2wf.contract.storage.graph.dto.GraphConstraintOperand;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphStandardWorkflowTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.contract.storage.graph.repository.ConstraintsRepository;
import com.ml2wf.contract.storage.graph.repository.StandardWorkflowTasksRepository;
import com.ml2wf.contract.storage.graph.repository.VersionsRepository;
import com.ml2wf.core.workflow.StandardWorkflow;
import com.ml2wf.core.workflow.StandardWorkflowTask;

public abstract class AbstractStandardWorkflowComponent<W extends GraphStandardWorkflowTask<W, V>,
        T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion,
        O extends GraphConstraintOperand<O, T, V>> implements IStandardWorkflowComponent {

    private static final String ROOT_WORKFLOW_NODE_NAME = "__ROOT_WORKFLOW";
    private static final String UNMANAGED_NODE_DESCRIPTION = "reserved workflow root. internal use only. not exported.";
    private static final String ROOT_CONSTRAINT_NODE_NAME = "__ROOT_CONSTRAINT";

    private final StandardWorkflowTasksRepository<W, V, ?> standardWorkflowTasksRepository;
    private final IGraphStandardWorkflowConverter<W, V> graphStandardWorkflowConverter;
    private final IStandardKnowledgeMergerComponent standardKnowledgeMergerComponent;
    private final VersionsRepository<V, ?> versionsRepository;
    private final ConstraintsRepository<O, T, V, ?> constraintsRepository;
    private final IGraphConstraintsConverter<O, T, V> constraintsConverter;

    protected AbstractStandardWorkflowComponent(StandardWorkflowTasksRepository<W, V, ?> standardWorkflowTasksRepository,
                                                IGraphStandardWorkflowConverter<W, V> graphStandardWorkflowConverter,
                                                IStandardKnowledgeMergerComponent standardKnowledgeMergerComponent,
                                                VersionsRepository<V, ?> versionsRepository,
                                                ConstraintsRepository<O, T, V, ?> constraintsRepository,
                                                IGraphConstraintsConverter<O,T, V> constraintsConverter) {
        this.standardWorkflowTasksRepository = standardWorkflowTasksRepository;
        this.graphStandardWorkflowConverter = graphStandardWorkflowConverter;
        this.standardKnowledgeMergerComponent = standardKnowledgeMergerComponent;
        this.versionsRepository = versionsRepository;
        this.constraintsRepository = constraintsRepository;
        this.constraintsConverter = constraintsConverter;
    }

    private W createUnmanagedNode(V graphVersion) {
        var convertedNode = graphStandardWorkflowConverter.fromStandardWorkflowTask(
                new StandardWorkflowTask(
                        ROOT_WORKFLOW_NODE_NAME,
                        UNMANAGED_NODE_DESCRIPTION,
                        true,
                        true
                )
        );
        convertedNode.setVersion(graphVersion);
        return standardWorkflowTasksRepository.save(convertedNode);
    }

    @Override
    public boolean importStandardWorkflow(String newVersionName, StandardWorkflow standardWorkflow) {
        standardKnowledgeMergerComponent.mergeWorkflowWithTree(newVersionName, standardWorkflow);
        var workflowRootTask = standardWorkflowTasksRepository.findOneByNameAndVersionName(
                ROOT_WORKFLOW_NODE_NAME, newVersionName
        ).orElseGet(() -> createUnmanagedNode(versionsRepository.findOneByName(newVersionName).orElseThrow())); // TODO: throw
        W graphWorkflowTask = graphStandardWorkflowConverter.fromStandardWorkflow(standardWorkflow);
        workflowRootTask.setNextTask(graphWorkflowTask);
        standardWorkflowTasksRepository.save(workflowRootTask);
        return true;
    }

    @Override
    public boolean isStandardWorkflowConsistent(String versionName, StandardWorkflow standardWorkflow) {
        var operands = constraintsRepository.findAllByTypeAndVersionName(
                ROOT_CONSTRAINT_NODE_NAME, versionName).get(0).getOperands();
        return operands.stream()
                .map(constraintsConverter::toConstraintTree)
                .allMatch(c -> c.isWorkflowConsistent(standardWorkflow));
    }
}
