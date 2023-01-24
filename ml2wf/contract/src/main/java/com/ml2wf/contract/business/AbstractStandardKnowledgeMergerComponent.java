package com.ml2wf.contract.business;

import com.ml2wf.contract.exception.NoVersionFoundException;
import com.ml2wf.contract.exception.VersionNotFoundException;
import com.ml2wf.contract.storage.graph.converter.IGraphStandardKnowledgeConverter;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.contract.storage.graph.repository.StandardKnowledgeTasksRepository;
import com.ml2wf.contract.storage.graph.repository.VersionsRepository;
import com.ml2wf.core.tree.StandardKnowledgeTask;
import com.ml2wf.core.workflow.StandardWorkflow;

import java.util.ArrayList;
import java.util.Collections;

import static com.ml2wf.core.util.XMLManager.getReferredTask;

public class AbstractStandardKnowledgeMergerComponent<T extends GraphStandardKnowledgeTask<T, V>,
        V extends GraphTaskVersion> implements IStandardKnowledgeMergerComponent {

    private static final String ROOT_NODE_NAME = "__ROOT";
    public static final String UNMANAGED_NODE_NAME = "__UNMANAGED";
    public static final String UNMANAGED_NODE_DESCRIPTION = "Parent of tasks with unknown referred parents.";
    private final StandardKnowledgeTasksRepository<T, V, ?> standardKnowledgeTasksRepository;
    private final VersionsRepository<V, ?> versionsRepository;
    private final IGraphStandardKnowledgeConverter<T, V> graphStandardKnowledgeConverter;
    private final IStandardKnowledgeComponent standardKnowledgeComponent;

    public AbstractStandardKnowledgeMergerComponent(StandardKnowledgeTasksRepository<T, V, ?> standardKnowledgeTasksRepository,
                                                    VersionsRepository<V, ?> versionsRepository,
                                                    IGraphStandardKnowledgeConverter<T, V> graphStandardKnowledgeConverter,
                                                    IStandardKnowledgeComponent standardKnowledgeComponent) {
        this.standardKnowledgeTasksRepository = standardKnowledgeTasksRepository;
        this.versionsRepository = versionsRepository;
        this.graphStandardKnowledgeConverter = graphStandardKnowledgeConverter;
        this.standardKnowledgeComponent = standardKnowledgeComponent;
    }

    private T createUnmanagedNode(V graphVersion) {
        var optGraphKnowledgeTask = standardKnowledgeTasksRepository.findOneByNameAndVersionName(
                ROOT_NODE_NAME, graphVersion.getName()
        );
        var graphKnowledgeTask = optGraphKnowledgeTask.orElseThrow(
                () -> new VersionNotFoundException(graphVersion.getName()));
        var firstGraphTreeTask = new ArrayList<>(graphKnowledgeTask.getChildren()).get(0);
        var convertedNode = graphStandardKnowledgeConverter.fromStandardKnowledgeTask(
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
            var convertedReferredTask = graphStandardKnowledgeConverter.fromStandardKnowledgeTask(
                    new StandardKnowledgeTask(t.getName(), t.getDescription(), t.isAbstract(), t.isOptional(), null, Collections.emptyList())
            ).get(0);
            parentTask.getChildren().add(convertedReferredTask);
            standardKnowledgeTasksRepository.save(parentTask);
        });
    }
}
