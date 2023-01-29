package com.ml2wf.neo4j.business.components;

import com.ml2wf.contract.business.IStandardWorkflowComponent;
import com.ml2wf.contract.exception.VersionNotFoundException;
import com.ml2wf.core.workflow.StandardWorkflow;
import com.ml2wf.core.workflow.StandardWorkflowTask;
import com.ml2wf.neo4j.storage.converter.impl.Neo4JConstraintsConverter;
import com.ml2wf.neo4j.storage.converter.impl.Neo4JWorkflowTasksConverter;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardWorkflowTask;
import com.ml2wf.neo4j.storage.repository.Neo4JConstraintsRepository;
import com.ml2wf.neo4j.storage.repository.Neo4JStandardWorkflowTasksRepository;
import com.ml2wf.neo4j.storage.repository.Neo4JVersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("neo4j")
@Component
public class Neo4JStandardWorkflowComponent implements IStandardWorkflowComponent {

    private static final String ROOT_WORKFLOW_NODE_NAME = "__ROOT_WORKFLOW";
    private static final String UNMANAGED_NODE_DESCRIPTION = "reserved workflow root. internal use only. not exported.";
    private static final String ROOT_CONSTRAINT_NODE_NAME = "__ROOT_CONSTRAINT";
    private final Neo4JWorkflowTasksConverter workflowTasksConverter;
    private final Neo4JStandardWorkflowTasksRepository standardWorkflowTasksRepository;
    private final Neo4JStandardKnowledgeMergerComponent standardKnowledgeMergerComponent;
    private final Neo4JVersionsRepository versionsRepository;
    private final Neo4JConstraintsRepository constraintsRepository;
    private final Neo4JConstraintsConverter constraintsConverter;

    public Neo4JStandardWorkflowComponent(@Autowired Neo4JStandardWorkflowTasksRepository standardWorkflowTasksRepository,
                                          @Autowired Neo4JWorkflowTasksConverter workflowTasksConverter,
                                          @Autowired Neo4JStandardKnowledgeMergerComponent standardKnowledgeMergerComponent,
                                          @Autowired Neo4JVersionsRepository versionsRepository,
                                          @Autowired Neo4JConstraintsRepository constraintsRepository,
                                          @Autowired Neo4JConstraintsConverter constraintsConverter) {
        this.workflowTasksConverter = workflowTasksConverter;
        this.standardWorkflowTasksRepository = standardWorkflowTasksRepository;
        this.standardKnowledgeMergerComponent = standardKnowledgeMergerComponent;
        this.versionsRepository = versionsRepository;
        this.constraintsRepository = constraintsRepository;
        this.constraintsConverter = constraintsConverter;
    }

    private Neo4JStandardWorkflowTask createUnmanagedNode(Neo4JTaskVersion graphVersion) {
        var convertedNode = workflowTasksConverter.fromStandardWorkflowTask(
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
    public StandardWorkflow getStandardWorkflow(String versionName) {
        var rootGraphWorkflow = standardWorkflowTasksRepository.findOneByNameAndVersionName(
                ROOT_WORKFLOW_NODE_NAME, versionName
        ).orElseThrow(() -> new VersionNotFoundException(versionName));
        return workflowTasksConverter.toStandardWorkflow(rootGraphWorkflow.getNextTask());
    }

    @Override
    public boolean importStandardWorkflow(String newVersionName, StandardWorkflow standardWorkflow) {
        standardKnowledgeMergerComponent.mergeWorkflowWithTree(newVersionName, standardWorkflow);
        var workflowRootTask = standardWorkflowTasksRepository.findOneByNameAndVersionName(
                ROOT_WORKFLOW_NODE_NAME, newVersionName
        ).orElseGet(() -> createUnmanagedNode(versionsRepository.findOneByName(newVersionName).orElseThrow())); // TODO: throw
        Neo4JStandardWorkflowTask graphWorkflowTask = workflowTasksConverter.fromStandardWorkflow(standardWorkflow);
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
