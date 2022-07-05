package com.ml2wf.v3.app.business.components.arango;

import com.google.common.collect.ImmutableList;
import com.ml2wf.v3.app.business.components.ConfigurationsComponent;
import com.ml2wf.v3.app.business.storage.graph.arango.converter.impl.ArangoTasksConverter;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.*;
import com.ml2wf.v3.app.business.storage.graph.arango.repository.*;
import com.ml2wf.v3.app.configurations.Configuration;
import com.ml2wf.v3.app.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Profile("arango")
@Component
public class ArangoConfigurationsComponent
        extends ConfigurationsComponent<ArangoConfiguration, ArangoStandardKnowledgeTask, ArangoTaskVersion, ArangoConfigurationFeature, ArangoConstraintOperand> {

    private final ArangoConfigurationToFeaturesLinksRepository configurationToFeaturesRepository;
    private final ArangoConfigurationFeaturesToTaskLinksRepository configurationFeaturesToTaskLinksRepository;

    public ArangoConfigurationsComponent(@Autowired ArangoConfigurationRepository configurationRepository,
                                         @Autowired ArangoConfigurationFeaturesRepository configurationFeaturesRepository,
                                         @Autowired ArangoConfigurationToFeaturesLinksRepository configurationToFeaturesRepository,
                                         @Autowired ArangoConfigurationFeaturesToTaskLinksRepository configurationFeaturesToTaskLinksRepository,
                                         @Autowired ArangoStandardKnowledgeComponent standardKnowledgeComponent,
                                         @Autowired ArangoVersionsComponent versionsComponent,
                                         @Autowired ArangoTasksConverter tasksConverter) {
        super(configurationRepository, configurationFeaturesRepository, standardKnowledgeComponent, versionsComponent, tasksConverter);
        this.configurationToFeaturesRepository = configurationToFeaturesRepository;
        this.configurationFeaturesToTaskLinksRepository = configurationFeaturesToTaskLinksRepository;
    }

    public boolean importConfiguration(String name, Configuration configuration) {
        // TODO: move common part in parent
        var version = versionsComponent.getLastVersion().orElseThrow(
                () -> new BadRequestException("No version found. Please import some tasks before saving a configuration."));
        var convertedConfigurationFeatures = configuration.getFeatures().stream()
                .map(c -> {
                    var correspondingTask = standardKnowledgeComponent.getTaskWithName(c.getName(), version.getName())
                            .orElseThrow(() -> new BadRequestException(String.format("No task found for name %s and version %s.", c.getName(), version.getName())));
                    var convertedTask = standardKnowledgeConverter.fromStandardKnowledgeTask(correspondingTask).get(0);
            return new ArangoConfigurationFeature(c.getAutomatic().getLowercaseName(), c.getManual().getLowercaseName(), convertedTask, version);
            })
                .collect(Collectors.toList());
        var storedConfigFeatures = configurationFeaturesRepository.saveAll(convertedConfigurationFeatures);
        var configFeaturesToTaskLinks = StreamSupport.stream(storedConfigFeatures.spliterator(), false)
                .map(c -> new ArangoConfigurationFeatureToTaskLink(c, c.getTask()))
                .collect(Collectors.toList());
        configurationFeaturesToTaskLinksRepository.saveAll(configFeaturesToTaskLinks);
        var storedConfiguration = configurationRepository.save(new ArangoConfiguration(name, version, ImmutableList.copyOf(storedConfigFeatures)));
        var configurationToFeaturesLink = storedConfiguration.getFeatures().stream()
                .map(f -> new ArangoConfigurationToFeatureLink(storedConfiguration, f))
                .collect(Collectors.toList());
        configurationToFeaturesRepository.saveAll(configurationToFeaturesLink);
        return true;
    }
}
