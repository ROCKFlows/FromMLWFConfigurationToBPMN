package com.ml2wf.neo4j.business.components;

import com.google.common.collect.ImmutableList;
import com.ml2wf.contract.business.IConfigurationComponent;
import com.ml2wf.contract.exception.ConfigurationNotFoundException;
import com.ml2wf.contract.exception.NoVersionFoundException;
import com.ml2wf.core.configurations.NamedConfiguration;
import com.ml2wf.neo4j.storage.converter.impl.Neo4JConfigurationConverter;
import com.ml2wf.neo4j.storage.dto.*;
import com.ml2wf.neo4j.storage.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Profile("neo4j")
@Component
public class Neo4JConfigurationComponent implements IConfigurationComponent {

    private final Neo4JVersionsRepository versionsRepository;
    private final Neo4JStandardKnowledgeTasksRepository standardKnowledgeTasksRepository;
    private final Neo4JConfigurationFeaturesRepository configurationFeaturesRepository;
    private final Neo4JConfigurationRepository configurationRepository;
    private final Neo4JConfigurationConverter configurationConverter;

    public Neo4JConfigurationComponent(@Autowired Neo4JConfigurationRepository configurationRepository,
                                       @Autowired Neo4JConfigurationFeaturesRepository configurationFeaturesRepository,
                                       @Autowired Neo4JConfigurationConverter configurationConverter,
                                       @Autowired Neo4JStandardKnowledgeTasksRepository standardKnowledgeTasksRepository,
                                       @Autowired Neo4JVersionsRepository versionsRepository) {
        this.versionsRepository = versionsRepository;
        this.standardKnowledgeTasksRepository = standardKnowledgeTasksRepository;
        this.configurationFeaturesRepository = configurationFeaturesRepository;
        this.configurationRepository = configurationRepository;
        this.configurationConverter = configurationConverter;
    }

    @Override
    public Mono<Boolean> importConfiguration(NamedConfiguration configuration) {
        // TODO: check if configuration already exists with name
        return versionsRepository.getLastVersion()
                .switchIfEmpty(Mono.error(NoVersionFoundException::new))
                .map((v) ->
                    configuration.getFeatures().stream()
                            .map(c ->
                                standardKnowledgeTasksRepository.findOneByNameAndVersionName(c.getName(), v.getName())
                                        .switchIfEmpty(Mono.error(() -> new RuntimeException(String.format("No task found for name %s and version %s.", c.getName(), v.getName()))))
                                        .map((t) -> new Neo4JConfigurationFeature(c.getAutomatic().getLowercaseName(), c.getManual().getLowercaseName(), t, v))
                            )
                            .map(configurationFeaturesRepository::saveAll)
                            .map((c) -> c.collectList()
                                    .flatMap((ch) -> configurationRepository.save(new Neo4JConfiguration(configuration.getName(), v, ImmutableList.copyOf(ch))))
                            )
                )
                .map((r) -> true); // TODO: check result
    }

    @Override
    public Mono<NamedConfiguration> getConfiguration(String configurationName) {
        return configurationRepository.findOneByName(configurationName)
                .map(configurationConverter::toStandardConfiguration)
                .switchIfEmpty(Mono.error(() -> new ConfigurationNotFoundException(configurationName)));
    }

    @Override
    public Flux<NamedConfiguration> getAllConfigurations() {
        return configurationRepository.findAll()
                .map(configurationConverter::toStandardConfiguration);
    }
}
