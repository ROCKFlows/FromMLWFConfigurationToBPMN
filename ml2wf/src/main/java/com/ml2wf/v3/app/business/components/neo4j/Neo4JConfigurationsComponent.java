package com.ml2wf.v3.app.business.components.neo4j;

import com.google.common.collect.ImmutableList;
import com.ml2wf.v3.app.business.components.ConfigurationsComponent;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.*;
import com.ml2wf.v3.app.business.storage.graph.neo4j.converter.impl.Neo4JTasksConverter;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.*;
import com.ml2wf.v3.app.business.storage.graph.neo4j.repository.Neo4JConfigurationFeaturesRepository;
import com.ml2wf.v3.app.business.storage.graph.neo4j.repository.Neo4JConfigurationRepository;
import com.ml2wf.v3.app.configurations.Configuration;
import com.ml2wf.v3.app.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class Neo4JConfigurationsComponent
        extends ConfigurationsComponent<Neo4JConfiguration, Neo4JStandardKnowledgeTask, Neo4JTaskVersion, Neo4JConfigurationFeature, Neo4JConstraintOperand> {

    public Neo4JConfigurationsComponent(@Autowired Neo4JConfigurationRepository configurationRepository,
                                        @Autowired Neo4JConfigurationFeaturesRepository configurationFeaturesRepository,
                                        @Autowired Neo4JStandardKnowledgeComponent standardKnowledgeComponent,
                                        @Autowired Neo4JVersionsComponent versionsComponent,
                                        @Autowired Neo4JTasksConverter tasksConverter) {
        super(configurationRepository, configurationFeaturesRepository, standardKnowledgeComponent, versionsComponent, tasksConverter);
    }

    public boolean importConfiguration(String name, Configuration configuration) throws Throwable {
        // TODO: move common part in parent
        var version = versionsComponent.getLastVersion().orElseThrow(
                () -> new BadRequestException("No version found. Please import some tasks before saving a configuration."));
        var convertedConfigurationFeatures = configuration.getFeatures().stream()
                .map(c -> {
                    var correspondingTask = standardKnowledgeComponent.getTaskWithName(c.getName(), version.getName())
                            .orElseThrow(() -> new BadRequestException(String.format("No task found for name %s and version %s.", c.getName(), version.getName())));
                    var convertedTask = standardKnowledgeConverter.fromStandardKnowledgeTask(correspondingTask).get(0);
                    return new Neo4JConfigurationFeature(c.getAutomatic().getLowercaseName(), c.getManual().getLowercaseName(), convertedTask, version);
                })
                .collect(Collectors.toList());
        var storedConfigFeatures = configurationFeaturesRepository.saveAll(convertedConfigurationFeatures);
        var storedConfiguration = configurationRepository.save(new Neo4JConfiguration(name, version, ImmutableList.copyOf(storedConfigFeatures)));
        return true;
    }
}
