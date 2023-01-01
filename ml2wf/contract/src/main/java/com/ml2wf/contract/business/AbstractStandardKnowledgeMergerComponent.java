package com.ml2wf.contract.business;

import com.ml2wf.contract.storage.graph.converter.IGraphStandardKnowledgeConverter;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.contract.storage.graph.repository.StandardKnowledgeTasksRepository;
import com.ml2wf.contract.storage.graph.repository.VersionsRepository;
import com.ml2wf.core.tree.StandardKnowledgeTask;
import com.ml2wf.core.workflow.StandardWorkflow;

import java.util.Collections;

import static com.ml2wf.core.util.XMLManager.getReferredTask;

public class AbstractStandardKnowledgeMergerComponent<T extends GraphStandardKnowledgeTask<T, V>,
        V extends GraphTaskVersion> implements IStandardKnowledgeMergerComponent {

    public static final String UNMANAGED_NODE_NAME = "__UNMANAGED";
    public static final String UNMANAGED_NODE_DESCRIPTION = "Parent of tasks with unknown referred parents.";
    private final StandardKnowledgeTasksRepository<T, V, ?> standardKnowledgeTasksRepository;
    private final VersionsRepository<V, ?> versionsRepository;
    private final IGraphStandardKnowledgeConverter<T, V> graphStandardKnowledgeConverter;

    public AbstractStandardKnowledgeMergerComponent(StandardKnowledgeTasksRepository<T, V, ?> standardKnowledgeTasksRepository,
                                                    VersionsRepository<V, ?> versionsRepository,
                                                    IGraphStandardKnowledgeConverter<T, V> graphStandardKnowledgeConverter) {
        this.standardKnowledgeTasksRepository = standardKnowledgeTasksRepository;
        this.versionsRepository = versionsRepository;
        this.graphStandardKnowledgeConverter = graphStandardKnowledgeConverter;
    }

    private T createUnmanagedNode(V graphVersion) {
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
        convertedNode.setVersion(graphVersion);
        return standardKnowledgeTasksRepository.save(convertedNode);
    }

    // TODO: move version ?
    // TODO: return new version ?
    @Override
    public void mergeWorkflowWithTree(String version, StandardWorkflow workflow) {
        var graphVersion = versionsRepository.findOneByName(version)
                .orElseThrow(); // TODO: manage this case
        var optUnmanagedNode = standardKnowledgeTasksRepository.findOneByNameAndVersion_Name(version, UNMANAGED_NODE_NAME);
        var unmanagedNode = optUnmanagedNode.orElseGet(() -> createUnmanagedNode(graphVersion));
        workflow.getTasks().forEach((t) -> {
            var optReferredName = getReferredTask(t.getDescription());
            var parentTask = optReferredName.map((n) -> standardKnowledgeTasksRepository.findOneByNameAndVersion_Name(version, n)
                    .orElseThrow()  // TODO: manage this case
                    ).orElse(unmanagedNode);  // TODO: log this case
            // TODO: convert using dedicated core util class
            var convertedReferredTask = graphStandardKnowledgeConverter.fromStandardKnowledgeTask(
                    new StandardKnowledgeTask(t.getName(), t.getDescription(), t.isAbstract(), t.isOptional(), version, Collections.emptyList())
            ).get(0);
            convertedReferredTask.setVersion(graphVersion); // TODO: bump version ?
            parentTask.getChildren().add(convertedReferredTask);
            standardKnowledgeTasksRepository.save(parentTask);
        });
    }
}
