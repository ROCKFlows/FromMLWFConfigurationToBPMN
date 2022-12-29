package com.ml2wf.arango.business.components;

import com.google.common.collect.ImmutableList;
import com.ml2wf.arango.storage.dto.*;
import com.ml2wf.arango.storage.repository.ArangoConfigurationFeaturesRepository;
import com.ml2wf.arango.storage.repository.ArangoConfigurationFeaturesToTaskLinksRepository;
import com.ml2wf.arango.storage.repository.ArangoConfigurationRepository;
import com.ml2wf.arango.storage.repository.ArangoConfigurationToFeaturesLinksRepository;
import com.ml2wf.contract.business.AbstractConfigurationsComponent;
import com.ml2wf.core.configurations.Configuration;
import com.ml2wf.arango.storage.converter.impl.ArangoTasksConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Profile("arango")
@Component
public class ArangoConfigurationsComponent
        extends AbstractConfigurationsComponent<ArangoConfiguration, ArangoTaskVersion, ArangoConfigurationFeature> {

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
                () -> new RuntimeException("No version found. Please import some tasks before saving a configuration.")); // TODO: use custom exception and intercept in app
        var convertedConfigurationFeatures = configuration.getFeatures().stream()
                .map(c -> {
                    var correspondingTask = standardKnowledgeComponent.getTaskWithName(c.getName(), version.getName())
                            .orElseThrow(() -> new RuntimeException(String.format("No task found for name %s and version %s.", c.getName(), version.getName()))); // TODO: use custom exception and intercept in app
                    var convertedTask = standardKnowledgeConverter.fromStandardKnowledgeTask(correspondingTask).get(0);
            return new ArangoConfigurationFeature(c.getAutomatic().getLowercaseName(), c.getManual().getLowercaseName(), (ArangoStandardKnowledgeTask) convertedTask, version);
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
