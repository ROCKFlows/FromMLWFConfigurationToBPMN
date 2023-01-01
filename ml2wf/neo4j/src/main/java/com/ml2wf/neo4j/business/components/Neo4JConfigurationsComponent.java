package com.ml2wf.neo4j.business.components;

import com.google.common.collect.ImmutableList;
import com.ml2wf.contract.business.AbstractConfigurationsComponent;
import com.ml2wf.core.configurations.Configuration;
import com.ml2wf.neo4j.storage.converter.impl.Neo4JTasksConverter;
import com.ml2wf.neo4j.storage.dto.*;
import com.ml2wf.neo4j.storage.repository.Neo4JConfigurationFeaturesRepository;
import com.ml2wf.neo4j.storage.repository.Neo4JConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Profile("neo4j")
@Component
public class Neo4JConfigurationsComponent
        extends AbstractConfigurationsComponent<Neo4JConfiguration, Neo4JTaskVersion, Neo4JStandardKnowledgeTask,
        Neo4JConstraintOperand, Neo4JConfigurationFeature> {

    public Neo4JConfigurationsComponent(@Autowired Neo4JConfigurationRepository configurationRepository,
                                        @Autowired Neo4JConfigurationFeaturesRepository configurationFeaturesRepository,
                                        @Autowired Neo4JStandardKnowledgeComponent standardKnowledgeComponent,
                                        @Autowired Neo4JVersionsComponent versionsComponent,
                                        @Autowired Neo4JTasksConverter tasksConverter) {
        super(configurationRepository, configurationFeaturesRepository, standardKnowledgeComponent, versionsComponent, tasksConverter);
    }

    public boolean importConfiguration(String name, Configuration configuration) {
        // TODO: move common part in parent
        var version = versionsComponent.getLastVersion().orElseThrow(
                () -> new RuntimeException("No version found. Please import some tasks before saving a configuration."));
        var convertedConfigurationFeatures = configuration.getFeatures().stream()
                .map(c -> {
                    var correspondingTask = standardKnowledgeComponent.getTaskWithName(c.getName(), version.getName())
                            .orElseThrow(() -> new RuntimeException(String.format("No task found for name %s and version %s.", c.getName(), version.getName())));
                    var convertedTask = standardKnowledgeConverter.fromStandardKnowledgeTask(correspondingTask).get(0);
                    return new Neo4JConfigurationFeature(c.getAutomatic().getLowercaseName(), c.getManual().getLowercaseName(), (Neo4JStandardKnowledgeTask) convertedTask, version);
                })
                .collect(Collectors.toList());
        var storedConfigFeatures = configurationFeaturesRepository.saveAll(convertedConfigurationFeatures);
        var storedConfiguration = configurationRepository.save(new Neo4JConfiguration(name, version, ImmutableList.copyOf(storedConfigFeatures)));
        return true;
    }
}
