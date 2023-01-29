package com.ml2wf.neo4j.business.components;

import com.ml2wf.contract.business.IStandardKnowledgeMergerComponent;
import com.ml2wf.contract.exception.NoVersionFoundException;
import com.ml2wf.contract.exception.VersionNotFoundException;
import com.ml2wf.core.tree.StandardKnowledgeTask;
import com.ml2wf.core.workflow.StandardWorkflow;
import com.ml2wf.neo4j.storage.converter.impl.Neo4JKnowledgeTasksConverter;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import com.ml2wf.neo4j.storage.repository.Neo4JStandardKnowledgeTasksRepository;
import com.ml2wf.neo4j.storage.repository.Neo4JVersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

import static com.ml2wf.core.util.XMLManager.getReferredTask;

@Profile("neo4j")
@Component
public class Neo4JStandardKnowledgeMergerComponent implements IStandardKnowledgeMergerComponent {

    private static final String ROOT_NODE_NAME = "__ROOT";
    public static final String UNMANAGED_NODE_NAME = "__UNMANAGED";
    public static final String UNMANAGED_NODE_DESCRIPTION = "Parent of tasks with unknown referred parents.";
    private final Neo4JStandardKnowledgeTasksRepository standardKnowledgeTasksRepository;
    private final Neo4JKnowledgeTasksConverter tasksConverter;
    private final Neo4JVersionsRepository versionsRepository;
    private final Neo4JStandardKnowledgeComponent standardKnowledgeComponent;

    Neo4JStandardKnowledgeMergerComponent(@Autowired Neo4JStandardKnowledgeTasksRepository standardKnowledgeTasksRepository,
                                          @Autowired Neo4JVersionsRepository versionsRepository,
                                          @Autowired Neo4JKnowledgeTasksConverter tasksConverter,
                                          @Autowired Neo4JStandardKnowledgeComponent standardKnowledgeComponent) {
        this.standardKnowledgeTasksRepository = standardKnowledgeTasksRepository;
        this.tasksConverter = tasksConverter;
        this.versionsRepository = versionsRepository;
        this.standardKnowledgeComponent = standardKnowledgeComponent;
    }

    private Neo4JStandardKnowledgeTask createUnmanagedNode(Neo4JTaskVersion graphVersion) {
        var optGraphKnowledgeTask = standardKnowledgeTasksRepository.findOneByNameAndVersionName(
                ROOT_NODE_NAME, graphVersion.getName()
        );
        var graphKnowledgeTask = optGraphKnowledgeTask.orElseThrow(
                () -> new VersionNotFoundException(graphVersion.getName()));
        var firstGraphTreeTask = new ArrayList<>(graphKnowledgeTask.getChildren()).get(0);
        var convertedNode = tasksConverter.fromStandardKnowledgeTask(
                new StandardKnowledgeTask(
                        UNMANAGED_NODE_NAME,
                        UNMANAGED_NODE_DESCRIPTION,
                        true,
                        true,
                        graphVersion.getName(),
                        Collections.emptyList()
                )
        ).get(0);
        var savedUnmanagedRootNode = standardKnowledgeTasksRepository.save(convertedNode);
        firstGraphTreeTask.getChildren().add(savedUnmanagedRootNode);
        standardKnowledgeTasksRepository.save(firstGraphTreeTask);
        return savedUnmanagedRootNode;
    }

    // TODO: move version ?
    // TODO: return new version ?
    @Override
    public void mergeWorkflowWithTree(String newVersionName, StandardWorkflow workflow) {
        var graphVersion = versionsRepository.getLastVersion()
                .orElseThrow(NoVersionFoundException::new);
        var originalTree = standardKnowledgeComponent.getStandardKnowledgeTree(graphVersion.getName());
        standardKnowledgeComponent.importStandardKnowledgeTree(newVersionName, originalTree);
        var newGraphVersion = versionsRepository.getLastVersion()
                .orElseThrow(NoVersionFoundException::new); // TODO: manage this case
        var optUnmanagedNode = standardKnowledgeTasksRepository.findOneByNameAndVersionName(
                UNMANAGED_NODE_NAME, newGraphVersion.getName()
        );
        var unmanagedNode = optUnmanagedNode.orElseGet(() -> createUnmanagedNode(newGraphVersion));
        workflow.getTasks().forEach((t) -> {
            var optReferredName = getReferredTask(t.getDescription());
            var parentTask = optReferredName.map((n) -> standardKnowledgeTasksRepository.findOneByNameAndVersionName(n, graphVersion.getName())
                    .orElseThrow()  // TODO: manage this case
            ).orElse(unmanagedNode);  // TODO: log this case
            // TODO: convert using dedicated core util class
            var convertedReferredTask = tasksConverter.fromStandardKnowledgeTask(
                    new StandardKnowledgeTask(t.getName(), t.getDescription(), t.isAbstract(), t.isOptional(), null, Collections.emptyList())
            ).get(0);
            parentTask.getChildren().add(convertedReferredTask);
            standardKnowledgeTasksRepository.save(parentTask);
        });
    }
}
