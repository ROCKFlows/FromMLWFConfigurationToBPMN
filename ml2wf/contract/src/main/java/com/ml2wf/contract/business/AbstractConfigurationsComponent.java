package com.ml2wf.contract.business;

import com.ml2wf.contract.storage.graph.dto.*;
import com.ml2wf.contract.storage.graph.repository.ConfigurationFeaturesRepository;
import com.ml2wf.contract.storage.graph.repository.ConfigurationRepository;
import com.ml2wf.contract.storage.graph.repository.StandardKnowledgeTasksRepository;
import com.ml2wf.contract.storage.graph.repository.VersionsRepository;
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
    protected final StandardKnowledgeTasksRepository<T, V, ?> standardKnowledgeTaskRepository;
    protected final VersionsRepository<V, ?> versionsRepository;

    protected AbstractConfigurationsComponent(ConfigurationRepository<C, F, T, V, String> configurationRepository,
                                              ConfigurationFeaturesRepository<F, T, V, Long> configurationFeaturesRepository,
                                              StandardKnowledgeTasksRepository<T, V, ?> standardKnowledgeTaskRepository,
                                              VersionsRepository<V, ?> versionsRepository) {
        this.configurationRepository = configurationRepository;
        this.configurationFeaturesRepository = configurationFeaturesRepository;
        this.standardKnowledgeTaskRepository = standardKnowledgeTaskRepository;
        this.versionsRepository = versionsRepository;
    }
}
