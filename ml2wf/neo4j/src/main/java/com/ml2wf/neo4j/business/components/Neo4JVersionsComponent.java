package com.ml2wf.neo4j.business.components;

import com.ml2wf.contract.business.IVersionsComponent;
import com.ml2wf.contract.exception.NoVersionFoundException;
import com.ml2wf.core.tree.StandardKnowledgeVersion;
import com.ml2wf.neo4j.storage.converter.impl.Neo4JVersionConverter;
import com.ml2wf.neo4j.storage.repository.Neo4JVersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Profile("neo4j")
@Component
public class Neo4JVersionsComponent implements IVersionsComponent {

    private final Neo4JVersionsRepository versionsRepository;
    private final Neo4JVersionConverter versionConverter;

    public Neo4JVersionsComponent(@Autowired Neo4JVersionsRepository versionsRepository,
                                  @Autowired Neo4JVersionConverter versionConverter) {
        this.versionsRepository = versionsRepository;
        this.versionConverter = versionConverter;
    }

    @Override
    public Mono<StandardKnowledgeVersion> getLastVersion() {
        return versionsRepository.getLastVersion()
                .switchIfEmpty(Mono.error(NoVersionFoundException::new))
                .map(versionConverter::toStandardKnowledgeVersion);
    }

    @Override
    public Flux<StandardKnowledgeVersion> getVersions() {
        return versionsRepository.findAll()
                .map(versionConverter::toStandardKnowledgeVersion);
    }
}

