package com.ml2wf.v3.app.business.components;

import com.ml2wf.v3.app.business.storage.graph.contracts.converter.IGraphStandardKnowledgeConverter;
import com.ml2wf.v3.app.business.storage.graph.contracts.dto.*;
import com.ml2wf.v3.app.business.storage.graph.contracts.repository.ConfigurationFeaturesRepository;
import com.ml2wf.v3.app.business.storage.graph.contracts.repository.ConfigurationRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class ConfigurationsComponent<C extends GraphConfiguration<T, V, F>, T extends GraphStandardKnowledgeTask<T, V>,
        V extends GraphTaskVersion, F extends GraphConfigurationFeature<T, V>, O extends GraphConstraintOperand<T, V, O>>
        implements IConfigurationComponent {

    protected final ConfigurationRepository<C, T, V, F, String> configurationRepository;
    protected final ConfigurationFeaturesRepository<F, T, V, Long> configurationFeaturesRepository;
    protected final StandardKnowledgeComponent<T, O, V> standardKnowledgeComponent;
    protected final VersionsComponent<V> versionsComponent;
    protected final IGraphStandardKnowledgeConverter<T, V> standardKnowledgeConverter;

    protected ConfigurationsComponent(ConfigurationRepository<C, T, V, F, String> configurationRepository,
                                      ConfigurationFeaturesRepository<F, T, V, Long> configurationFeaturesRepository,
                                      StandardKnowledgeComponent<T, O, V> standardKnowledgeComponent,
                                      VersionsComponent<V> versionsComponent,
                                      IGraphStandardKnowledgeConverter<T, V> standardKnowledgeConverter) {
        this.configurationRepository = configurationRepository;
        this.configurationFeaturesRepository = configurationFeaturesRepository;
        this.standardKnowledgeComponent = standardKnowledgeComponent;
        this.versionsComponent = versionsComponent;
        this.standardKnowledgeConverter = standardKnowledgeConverter;
    }
}
