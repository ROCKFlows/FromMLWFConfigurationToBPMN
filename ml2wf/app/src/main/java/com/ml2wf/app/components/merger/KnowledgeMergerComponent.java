package com.ml2wf.app.components.merger;

import com.ml2wf.contract.storage.graph.converter.IGraphStandardKnowledgeConverter;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.contract.storage.graph.repository.StandardKnowledgeTasksRepository;
import com.ml2wf.core.tree.StandardKnowledgeTask;
import com.ml2wf.core.workflow.StandardWorkflow;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

public class KnowledgeMergerComponent<T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion> {

    private final StandardKnowledgeTasksRepository<T, V, ?> standardKnowledgeTasksRepository;
    private final IGraphStandardKnowledgeConverter<T, V> graphStandardKnowledgeConverter;

    public KnowledgeMergerComponent(@Autowired StandardKnowledgeTasksRepository<T, V, ?> standardKnowledgeTasksRepository,
                                    @Autowired IGraphStandardKnowledgeConverter<T, V> graphStandardKnowledgeConverter) {
        this.standardKnowledgeTasksRepository = standardKnowledgeTasksRepository;
        this.graphStandardKnowledgeConverter = graphStandardKnowledgeConverter;
    }

    // TODO: move version ?
    // TODO: return new version ?
    void mergeWorkflowWithTree(String version, StandardWorkflow workflow) {
        workflow.getTasks().forEach((t) -> {
            var optReferredTask = standardKnowledgeTasksRepository.findOneByNameAndVersion_Name(
                    version, String.format("TODO: reference of %s", t.getName())
            );
            var referredTask = optReferredTask.orElseThrow(); // TODO: manage this case
            // TODO: convert using dedicated core util class
            var convertedReferredTask = graphStandardKnowledgeConverter.fromStandardKnowledgeTask(
                    new StandardKnowledgeTask(t.getName(), t.getDescription(), t.isAbstract(), t.isOptional(), version, Collections.emptyList())
            ).get(0);
            referredTask.getChildren().add(convertedReferredTask);
        });
    }
}
