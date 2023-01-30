package com.ml2wf.neo4j.business.components;

import com.ml2wf.contract.business.IStandardKnowledgeComponent;
import com.ml2wf.contract.exception.DuplicatedVersionNameException;
import com.ml2wf.contract.exception.KnowledgeTaskNotFoundException;
import com.ml2wf.contract.exception.VersionNotFoundException;
import com.ml2wf.core.tree.StandardKnowledgeTask;
import com.ml2wf.core.tree.StandardKnowledgeTree;
import com.ml2wf.neo4j.storage.converter.impl.Neo4JConstraintsConverter;
import com.ml2wf.neo4j.storage.converter.impl.Neo4JKnowledgeTasksConverter;
import com.ml2wf.neo4j.storage.dto.Neo4JConstraintOperand;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import com.ml2wf.neo4j.storage.repository.Neo4JConstraintsRepository;
import com.ml2wf.neo4j.storage.repository.Neo4JStandardKnowledgeTasksRepository;
import com.ml2wf.neo4j.storage.repository.Neo4JVersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Profile("neo4j")
@Component
public class Neo4JStandardKnowledgeComponent implements IStandardKnowledgeComponent {

    private static final String ROOT_NODE_NAME = "__ROOT";
    private static final String ROOT_CONSTRAINT_NODE_NAME = "__ROOT_CONSTRAINT";
    private final Neo4JStandardKnowledgeTasksRepository standardKnowledgeTasksRepository;
    private final Neo4JConstraintsRepository constraintsRepository;
    private final Neo4JVersionsRepository versionsRepository;
    private final Neo4JConstraintsConverter constraintsConverter;
    private final Neo4JKnowledgeTasksConverter tasksConverter;

    Neo4JStandardKnowledgeComponent(@Autowired Neo4JStandardKnowledgeTasksRepository standardKnowledgeTasksRepository,
                                    @Autowired Neo4JConstraintsRepository constraintsRepository,
                                    @Autowired Neo4JVersionsRepository versionsRepository,
                                    @Autowired Neo4JConstraintsConverter constraintsConverter,
                                    @Autowired Neo4JKnowledgeTasksConverter tasksConverter) {
        this.standardKnowledgeTasksRepository = standardKnowledgeTasksRepository;
        this.constraintsRepository = constraintsRepository;
        this.versionsRepository = versionsRepository;
        this.constraintsConverter = constraintsConverter;
        this.tasksConverter = tasksConverter;
    }

    @Override
    public Mono<StandardKnowledgeTree> getStandardKnowledgeTree(String versionName) {
        return standardKnowledgeTasksRepository.findOneByNameAndVersionName(ROOT_NODE_NAME, versionName)
                .switchIfEmpty(Mono.error(() -> new VersionNotFoundException(versionName)))
                .map((t) -> new ArrayList<>(t.getChildren()).get(0))
                .flatMap((t) -> constraintsRepository.findAllByTypeAndVersionName(ROOT_CONSTRAINT_NODE_NAME, versionName)
                        .next() // taking first as it is the root
                        .map((c) -> c.getOperands().stream().map(constraintsConverter::toConstraintTree))
                        .map((c) -> tasksConverter.toStandardKnowledgeTree(t, c.collect(Collectors.toList())))
                );
    }

    @Override
    public Mono<StandardKnowledgeTask> getTaskWithName(String taskName, String versionName) {
        return standardKnowledgeTasksRepository.findOneByNameAndVersionName(taskName, versionName)
                .map(tasksConverter::toStandardKnowledgeTask);
    }

    @Override
    public Mono<StandardKnowledgeTree> getStandardKnowledgeTaskWithName(String taskName, String versionName) {
        // TODO: use a dedicated converter (KnowledgeTask to KnowledgeTree)
        return getTaskWithName(taskName, versionName)
                .switchIfEmpty(Mono.error(() -> new KnowledgeTaskNotFoundException(taskName, versionName)))
                .map((t) -> new StandardKnowledgeTree(Collections.singletonList(t), Collections.emptyList()));
    }

    public Mono<Boolean> importStandardKnowledgeTree(String versionName, StandardKnowledgeTree standardKnowledgeTree) {
        // TODO: split into dedicated components (one for tasks, one for constraints...)
        // saving new version
        return versionsRepository.getLastVersion()
                .defaultIfEmpty(new Neo4JTaskVersion(0, 0, 0, "unversioned", null))
                .map((v) -> {
                    if (v.getName().equals(versionName)) {
                        throw new DuplicatedVersionNameException(versionName);
                    }
                    var previousVersion = "unversioned".equals(v.getName()) ? null : v;
                    return versionsRepository.save(new Neo4JTaskVersion(v.getMajor() + 1, 0, 0, versionName, previousVersion));
                })
                .map((mv) -> standardKnowledgeTasksRepository.saveAll(tasksConverter.fromStandardKnowledgeTree(standardKnowledgeTree))
                        .next()
                        .flatMap((t) ->
                                mv.map((v) -> standardKnowledgeTasksRepository.save(new Neo4JStandardKnowledgeTask(ROOT_NODE_NAME, true, true, v, "reserved tree root. internal use only. not exported.", Collections.singletonList(t)))
                                                .map((st) -> constraintsRepository.saveAll(constraintsConverter.fromStandardKnowledgeTree(
                                                        standardKnowledgeTree, Collections.singletonList(st)
                                                )))
                                        .map((mc) -> mc.map((c) -> constraintsRepository.save(new Neo4JConstraintOperand(ROOT_CONSTRAINT_NODE_NAME, v, Collections.singletonList(c)))))
                                )
                       )
                ).thenReturn(true);
    }
}
