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

import java.util.List;
import java.util.stream.Collectors;

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
    public boolean importConfiguration(NamedConfiguration configuration) {
        // TODO: check if configuration already exists with name
        var graphVersion = versionsRepository.getLastVersion().orElseThrow(NoVersionFoundException::new);
        var convertedConfigurationFeatures = configuration.getFeatures().stream()
                .map(c -> {
                    var correspondingGraphTask = standardKnowledgeTasksRepository.findOneByNameAndVersionName(c.getName(), graphVersion.getName())
                            .orElseThrow(() -> new RuntimeException(String.format("No task found for name %s and version %s.", c.getName(), graphVersion.getName())));
                    return new Neo4JConfigurationFeature(c.getAutomatic().getLowercaseName(), c.getManual().getLowercaseName(), correspondingGraphTask, graphVersion);
                })
                .collect(Collectors.toList());
        var storedConfigFeatures = configurationFeaturesRepository.saveAll(convertedConfigurationFeatures);
        // TODO: use configuration converter
        configurationRepository.save(new Neo4JConfiguration(configuration.getName(), graphVersion, ImmutableList.copyOf(storedConfigFeatures)));
        return true;
    }

    @Override
    public NamedConfiguration getConfiguration(String configurationName) {
        return configurationRepository.findOneByName(configurationName)
                .map(configurationConverter::toStandardConfiguration)
                .orElseThrow(() -> new ConfigurationNotFoundException(configurationName));
    }

    @Override
    public List<NamedConfiguration> getAllConfigurations() {
        return configurationRepository.findAll().stream()
                .map(configurationConverter::toStandardConfiguration)
                .collect(Collectors.toList());
    }
}
