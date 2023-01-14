package com.ml2wf.contract.business;

import com.ml2wf.contract.exception.VersionNotFoundException;
import com.ml2wf.contract.storage.graph.converter.IGraphConstraintsConverter;
import com.ml2wf.contract.storage.graph.converter.IGraphStandardKnowledgeConverter;
import com.ml2wf.contract.storage.graph.dto.GraphConstraintOperand;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.contract.storage.graph.repository.ConstraintsRepository;
import com.ml2wf.contract.storage.graph.repository.StandardKnowledgeTasksRepository;
import com.ml2wf.contract.storage.graph.repository.VersionsRepository;
import com.ml2wf.core.tree.StandardKnowledgeTask;
import com.ml2wf.core.tree.StandardKnowledgeTree;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public abstract class AbstractStandardKnowledgeComponent<T extends GraphStandardKnowledgeTask<T, V>,
        O extends GraphConstraintOperand<O, T, V>, V extends GraphTaskVersion>
        implements IStandardKnowledgeComponent {

    protected static final String ROOT_NODE_NAME = "__ROOT";
    protected static final String ROOT_CONSTRAINT_NODE_NAME = "__ROOT_CONSTRAINT";

    protected final StandardKnowledgeTasksRepository<T, V, ?> standardKnowledgeTasksRepository;
    protected final ConstraintsRepository<O, T, V, ?> constraintsRepository;
    protected final VersionsRepository<V, String> versionsRepository;
    protected final IGraphConstraintsConverter<O, T, V> constraintsConverter;
    protected final IGraphStandardKnowledgeConverter<T, V> tasksConverter;

    protected AbstractStandardKnowledgeComponent(
            StandardKnowledgeTasksRepository<T, V, ?> standardKnowledgeTasksRepository,
            ConstraintsRepository<O, T, V, ?> constraintsRepository,
            VersionsRepository<V, String> versionsRepository,
            IGraphConstraintsConverter<O, T, V> constraintsConverter,
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
        System.out.println("standardKnowledgeTasksRepository.findOneByNameAndVersionName(ROOT_NODE_NAME, versionName) = " + standardKnowledgeTasksRepository.findOneByNameAndVersionName(ROOT_NODE_NAME, versionName));
        var optGraphKnowledgeTask = standardKnowledgeTasksRepository.findOneByNameAndVersionName(ROOT_NODE_NAME, versionName);
        System.out.println("optGraphKnowledgeTask = " + optGraphKnowledgeTask);
        var graphKnowledgeTask = optGraphKnowledgeTask.orElseThrow(
                () -> new VersionNotFoundException(versionName));
        System.out.println("graphKnowledgeTask = " + graphKnowledgeTask);
        System.out.println("graphKnowledgeTask = " + graphKnowledgeTask.getName());
        System.out.println("graphKnowledgeTask = " + graphKnowledgeTask.getVersion());
        System.out.println("graphKnowledgeTask = " + graphKnowledgeTask.getChildren());
        // __ROOT node is for internal use only and should not be exported
        var firstGraphTreeTask = new ArrayList<>(graphKnowledgeTask.getChildren()).get(0);
        var rootConstraint = constraintsRepository.findAllByTypeAndVersionName(ROOT_CONSTRAINT_NODE_NAME, versionName);
        return tasksConverter.toStandardKnowledgeTree(firstGraphTreeTask, rootConstraint.get(0).getOperands().stream()
                .map(constraintsConverter::toConstraintTree)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<StandardKnowledgeTask> getTaskWithName(String taskName, String versionName) {
        return standardKnowledgeTasksRepository.findOneByNameAndVersionName(taskName, versionName)
                .map(tasksConverter::toStandardKnowledgeTask);
    }

    @Override
    public StandardKnowledgeTree getStandardKnowledgeTaskWithName(String taskName, String versionName) {
        // TODO: use a dedicated converter (KnowledgeTask to KnowledgeTree)
        return new StandardKnowledgeTree(
                Collections.singletonList(getTaskWithName(taskName, versionName).orElseThrow(
                        () -> new RuntimeException(String.format("No task found for name %s and version %s.", taskName, versionName))
                )),
                Collections.emptyList()
        );
    }
}
