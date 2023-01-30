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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

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

    private Mono<Neo4JStandardKnowledgeTask> createUnmanagedNode(Neo4JTaskVersion graphVersion) {
        return standardKnowledgeTasksRepository.findOneByNameAndVersionName(ROOT_NODE_NAME, graphVersion.getName())
                .switchIfEmpty(Mono.error(() -> new VersionNotFoundException(graphVersion.getName())))
                .map((t) -> new ArrayList<>(t.getChildren()).get(0))
                .flatMap((t) ->
                        standardKnowledgeTasksRepository.save(
                                tasksConverter.fromStandardKnowledgeTask(
                                        new StandardKnowledgeTask(
                                                UNMANAGED_NODE_NAME,
                                                UNMANAGED_NODE_DESCRIPTION,
                                                true,
                                                true,
                                                graphVersion.getName(),
                                                Collections.emptyList()
                                        )
                                ).get(0)
                        ).map((f) -> {
                            t.getChildren().add(f);
                            // TODO: fix value never used as a publisher
                            standardKnowledgeTasksRepository.save(t);
                            return f;
                        })
                );
    }

    // TODO: move version ?
    // TODO: return new version ?
    @Override
    public Mono<Void> mergeWorkflowWithTree(String newVersionName, StandardWorkflow workflow) {
        return versionsRepository.getLastVersion()
                .switchIfEmpty(Mono.error(NoVersionFoundException::new))
                .map((v) -> standardKnowledgeComponent.getStandardKnowledgeTree(v.getName()))
                .map((mt) -> mt.map((t) -> standardKnowledgeComponent.importStandardKnowledgeTree(newVersionName, t)))
                .flatMap((b) -> versionsRepository.getLastVersion()
                        .switchIfEmpty(Mono.error(NoVersionFoundException::new)))
                .flatMap((v) -> standardKnowledgeTasksRepository.findOneByNameAndVersionName(UNMANAGED_NODE_NAME, v.getName())
                        .switchIfEmpty(Mono.defer(() -> createUnmanagedNode(v)))
                        .map((t) -> Flux.fromIterable(workflow.getTasks())
                                .map((wt) -> getReferredTask(t.getDescription())
                                        .map((n) -> standardKnowledgeTasksRepository.findOneByNameAndVersionName(n, v.getName())
                                                .switchIfEmpty(Mono.error(NoSuchElementException::new)) // TODO: manage this case
                                        )
                                        .orElse(Mono.just(t))  // TODO: log this case
                                        .map((pt) -> {
                                            pt.getChildren().add(tasksConverter.fromStandardKnowledgeTask(
                                                    new StandardKnowledgeTask(wt.getName(), wt.getDescription(), wt.isAbstract(), wt.isOptional(), null, Collections.emptyList())
                                            ).get(0));
                                            return pt;
                                        })
                                )
                                .flatMap(standardKnowledgeTasksRepository::saveAll)
                        )
                ).thenEmpty(Mono.empty());
    }
}
