package com.ml2wf.v3.app.business.components;

import com.google.common.collect.ImmutableList;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoConfiguration;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoConfigurationFeature;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoConfigurationFeatureToTaskLink;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoConfigurationToFeatureLink;
import com.ml2wf.v3.app.business.storage.graph.repository.ConfigurationFeaturesRepository;
import com.ml2wf.v3.app.business.storage.graph.repository.ConfigurationFeaturesToTaskLinksRepository;
import com.ml2wf.v3.app.business.storage.graph.repository.ConfigurationRepository;
import com.ml2wf.v3.app.business.storage.graph.repository.ConfigurationToFeaturesRepository;
import com.ml2wf.v3.app.configurations.Configuration;
import com.ml2wf.v3.app.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class ConfigurationsComponent {

    private final ConfigurationRepository configurationRepository;
    private final ConfigurationToFeaturesRepository configurationToFeaturesRepository;
    private final ConfigurationFeaturesRepository configurationFeaturesRepository;
    private final ConfigurationFeaturesToTaskLinksRepository configurationFeaturesToTaskLinksRepository;
    private final StandardKnowledgeComponent standardKnowledgeComponent;
    private final VersionsComponent versionsComponent;

    public ConfigurationsComponent(@Autowired ConfigurationRepository configurationRepository,
                                   @Autowired ConfigurationFeaturesRepository configurationFeaturesRepository,
                                   @Autowired ConfigurationToFeaturesRepository configurationToFeaturesRepository,
                                   @Autowired ConfigurationFeaturesToTaskLinksRepository configurationFeaturesToTaskLinksRepository,
                                   @Autowired StandardKnowledgeComponent standardKnowledgeComponent,
                                   @Autowired VersionsComponent versionsComponent) {
        this.configurationRepository = configurationRepository;
        this.configurationToFeaturesRepository = configurationToFeaturesRepository;
        this.configurationFeaturesRepository = configurationFeaturesRepository;
        this.configurationFeaturesToTaskLinksRepository = configurationFeaturesToTaskLinksRepository;
        this.standardKnowledgeComponent = standardKnowledgeComponent;
        this.versionsComponent = versionsComponent;
    }

    public boolean importConfiguration(String name, Configuration configuration) {
        var version = versionsComponent.getLastVersion().orElseThrow(
                () -> new BadRequestException("No version found. Please import some tasks before saving a configuration."));
        var convertedConfigurationFeatures = configuration.getFeatures().stream()
                .map(c -> {
                    var correspondingTask = standardKnowledgeComponent.getArangoTaskWithName(c.getName(), version.getName())
                            .orElseThrow(() -> new BadRequestException(String.format("No task found for name %s and version %s.", c.getName(), version.getName())));
            return new ArangoConfigurationFeature(c.getAutomatic().getLowercaseName(), c.getManual().getLowercaseName(), correspondingTask, version);
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
