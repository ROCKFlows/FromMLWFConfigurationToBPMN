package com.ml2wf.v3.app.business.components;

//@Component
public class ConfigurationsComponent {
/*
    private final ConfigurationRepository configurationRepository;
    private final ConfigurationToFeaturesRepository configurationToFeaturesRepository;
    private final ConfigurationFeaturesRepository configurationFeaturesRepository;
    private final ConfigurationFeaturesToTaskLinksRepository configurationFeaturesToTaskLinksRepository;
    private final ArangoStandardKnowledgeComponent standardKnowledgeComponent;
    private final VersionsComponent versionsComponent;

    public ConfigurationsComponent(@Autowired ConfigurationRepository configurationRepository,
                                   @Autowired ConfigurationFeaturesRepository configurationFeaturesRepository,
                                   @Autowired ConfigurationToFeaturesRepository configurationToFeaturesRepository,
                                   @Autowired ConfigurationFeaturesToTaskLinksRepository configurationFeaturesToTaskLinksRepository,
                                   @Autowired ArangoStandardKnowledgeComponent standardKnowledgeComponent,
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
    }*/
}
