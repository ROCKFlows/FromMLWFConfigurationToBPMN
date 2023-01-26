package com.ml2wf.contract.business;

import com.ml2wf.contract.exception.ConfigurationNotFoundException;
import com.ml2wf.contract.storage.graph.converter.IGraphConfigurationConverter;
import com.ml2wf.contract.storage.graph.dto.*;
import com.ml2wf.contract.storage.graph.repository.ConfigurationFeaturesRepository;
import com.ml2wf.contract.storage.graph.repository.ConfigurationRepository;
import com.ml2wf.contract.storage.graph.repository.StandardKnowledgeTasksRepository;
import com.ml2wf.contract.storage.graph.repository.VersionsRepository;
import com.ml2wf.core.configurations.Configuration;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractConfigurationsComponent<
        C extends GraphConfiguration<F, T, V>,
        V extends GraphTaskVersion,
        T extends GraphStandardKnowledgeTask<T, V>,
        F extends GraphConfigurationFeature<T, V>
        >
        implements IConfigurationComponent {

    protected final ConfigurationRepository<C, F, T, V, String> configurationRepository;
    protected final ConfigurationFeaturesRepository<F, T, V, Long> configurationFeaturesRepository;
    protected final IGraphConfigurationConverter<C, F, T, V> configurationConverter;
    protected final StandardKnowledgeTasksRepository<T, V, ?> standardKnowledgeTaskRepository;
    protected final VersionsRepository<V, ?> versionsRepository;

    protected AbstractConfigurationsComponent(ConfigurationRepository<C, F, T, V, String> configurationRepository,
                                              ConfigurationFeaturesRepository<F, T, V, Long> configurationFeaturesRepository,
                                              IGraphConfigurationConverter<C, F, T, V> configurationConverter,
                                              StandardKnowledgeTasksRepository<T, V, ?> standardKnowledgeTaskRepository,
                                              VersionsRepository<V, ?> versionsRepository) {
        this.configurationRepository = configurationRepository;
        this.configurationFeaturesRepository = configurationFeaturesRepository;
        this.configurationConverter = configurationConverter;
        this.standardKnowledgeTaskRepository = standardKnowledgeTaskRepository;
        this.versionsRepository = versionsRepository;
    }

    @Override
    public Configuration getConfiguration(String configurationName) {
        return configurationRepository.findOneByNameAndVersionName(configurationName)
                .map(configurationConverter::toStandardConfiguration)
                .orElseThrow(() -> new ConfigurationNotFoundException(configurationName));
    }
}
