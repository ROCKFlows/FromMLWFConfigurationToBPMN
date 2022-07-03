package com.ml2wf.v3.app.business.components;

import com.ml2wf.v3.app.business.storage.graph.contracts.converter.IGraphConstraintsConverter;
import com.ml2wf.v3.app.business.storage.graph.contracts.converter.IGraphStandardKnowledgeConverter;
import com.ml2wf.v3.app.business.storage.graph.contracts.dto.*;
import com.ml2wf.v3.app.business.storage.graph.contracts.repository.*;
import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphConstraintOperand;
import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphStandardKnowledgeTask;
import com.ml2wf.v3.app.exceptions.BadRequestException;
import com.ml2wf.v3.app.tree.StandardKnowledgeTask;
import com.ml2wf.v3.app.tree.StandardKnowledgeTree;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public abstract class StandardKnowledgeComponent<T extends GraphStandardKnowledgeTask<T, V>,
        C extends GraphConstraintOperand<T, V, C>, V extends GraphTaskVersion> implements IStandardKnowledgeComponent {

    protected static final String ROOT_NODE_NAME = "__ROOT";
    protected static final String ROOT_CONSTRAINT_NODE_NAME = "__ROOT_CONSTRAINT";

    protected final StandardKnowledgeTasksRepository<T, V, Long> standardKnowledgeTasksRepository;
    protected final ConstraintsRepository<C, T, V, Long> constraintsRepository;
    protected final VersionsRepository<V, String> versionsRepository;
    protected final IGraphConstraintsConverter<T, C, V> constraintsConverter;
    protected final IGraphStandardKnowledgeConverter<T, V> tasksConverter;

    protected StandardKnowledgeComponent(
            StandardKnowledgeTasksRepository<T, V, Long> standardKnowledgeTasksRepository,
            ConstraintsRepository<C, T, V, Long> constraintsRepository,
            VersionsRepository<V, String> versionsRepository,
            IGraphConstraintsConverter<T, C, V> constraintsConverter,
            IGraphStandardKnowledgeConverter<T, V> tasksConverter
    ) {
        // TODO: find a way to autowire these elements in abstract rather than in concretes
        this.standardKnowledgeTasksRepository = standardKnowledgeTasksRepository;
        this.constraintsRepository = constraintsRepository;
        this.versionsRepository = versionsRepository;
        this.constraintsConverter = constraintsConverter;
        this.tasksConverter = tasksConverter;
    }

    @Override
    public StandardKnowledgeTree getStandardKnowledgeTree(String versionName) {
        var optArangoStandardKnowledgeTask = standardKnowledgeTasksRepository.findOneByNameAndVersion_Name(ROOT_NODE_NAME, versionName);
        var arangoStandardKnowledgeTask = optArangoStandardKnowledgeTask.orElseThrow(
                () -> new BadRequestException("No task found for version " + versionName));
        // __ROOT node is for internal use only and should not be exported
        var firstArangoTreeTask = new ArrayList<>(arangoStandardKnowledgeTask.getChildren()).get(0);
        var rootConstraint = constraintsRepository.findAllByTypeEqualsAndVersion_Name(ROOT_CONSTRAINT_NODE_NAME, firstArangoTreeTask.getVersion().getName());
        return tasksConverter.toStandardKnowledgeTree(firstArangoTreeTask, rootConstraint.get(0).getOperands().stream()
                .map(constraintsConverter::toConstraintTree)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<StandardKnowledgeTask> getTaskWithName(String taskName, String versionName) {
        return standardKnowledgeTasksRepository.findOneByNameAndVersion_Name(taskName, versionName).map(tasksConverter::toStandardKnowledgeTask);
    }

    @Override
    public StandardKnowledgeTree getStandardKnowledgeTaskWithName(String taskName, String versionName) {
        // TODO: use a dedicated converter (KnowledgeTask to KnowledgeTree)
        return new StandardKnowledgeTree(
                Collections.singletonList(getTaskWithName(taskName, versionName).orElseThrow(
                        () -> new BadRequestException(String.format("No task found for name %s and version %s.", taskName, versionName))
                )),
                Collections.emptyList()
        );
    }
}
