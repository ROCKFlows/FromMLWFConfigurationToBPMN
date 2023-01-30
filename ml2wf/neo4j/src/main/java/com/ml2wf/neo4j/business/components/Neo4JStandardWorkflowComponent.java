package com.ml2wf.neo4j.business.components;

import com.ml2wf.contract.business.IStandardWorkflowComponent;
import com.ml2wf.contract.exception.VersionNotFoundException;
import com.ml2wf.core.workflow.StandardWorkflow;
import com.ml2wf.core.workflow.StandardWorkflowTask;
import com.ml2wf.neo4j.storage.converter.impl.Neo4JConstraintsConverter;
import com.ml2wf.neo4j.storage.converter.impl.Neo4JWorkflowTasksConverter;
import com.ml2wf.neo4j.storage.dto.Neo4JConstraintOperand;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardWorkflowTask;
import com.ml2wf.neo4j.storage.repository.Neo4JConstraintsRepository;
import com.ml2wf.neo4j.storage.repository.Neo4JStandardWorkflowTasksRepository;
import com.ml2wf.neo4j.storage.repository.Neo4JVersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

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

    private Mono<Neo4JStandardWorkflowTask> createUnmanagedNode(Mono<Neo4JTaskVersion> graphVersion) {
        return graphVersion.map((v) -> {
            var convertedNode = workflowTasksConverter.fromStandardWorkflowTask(
                    new StandardWorkflowTask(
                            ROOT_WORKFLOW_NODE_NAME,
                            UNMANAGED_NODE_DESCRIPTION,
                            true,
                            true
                    )
            );
            convertedNode.setVersion(v);
            return convertedNode;
        }).flatMap(standardWorkflowTasksRepository::save);
    }

    @Override
    public Mono<StandardWorkflow> getStandardWorkflow(String versionName) {
        return standardWorkflowTasksRepository.findOneByNameAndVersionName(ROOT_WORKFLOW_NODE_NAME, versionName)
                .switchIfEmpty(Mono.error(() -> new VersionNotFoundException(versionName)))
                .map((r) -> workflowTasksConverter.toStandardWorkflow(r.getNextTask()));
    }

    @Override
    public Mono<Boolean> importStandardWorkflow(String newVersionName, StandardWorkflow standardWorkflow) {
        standardKnowledgeMergerComponent.mergeWorkflowWithTree(newVersionName, standardWorkflow);
        return standardWorkflowTasksRepository.findOneByNameAndVersionName(ROOT_WORKFLOW_NODE_NAME, newVersionName)
                .switchIfEmpty(Mono.defer(() -> createUnmanagedNode(versionsRepository.findOneByName(newVersionName))))
                .map((t) -> {
                    t.setNextTask(workflowTasksConverter.fromStandardWorkflow(standardWorkflow));
                    return t;
                }).flatMap(standardWorkflowTasksRepository::save)
                .map(Objects::nonNull);
    }

    @Override
    public Mono<Boolean> isStandardWorkflowConsistent(String versionName, StandardWorkflow standardWorkflow) {
        return constraintsRepository.findAllByTypeAndVersionName(ROOT_CONSTRAINT_NODE_NAME, versionName)
                .next()
                .map(Neo4JConstraintOperand::getOperands)
                .map((c) -> c.stream().map(constraintsConverter::toConstraintTree)
                        .allMatch(e -> e.isWorkflowConsistent(standardWorkflow)));
    }
}
