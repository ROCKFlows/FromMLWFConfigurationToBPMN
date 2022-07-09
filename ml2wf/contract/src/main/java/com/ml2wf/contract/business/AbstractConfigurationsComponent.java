package com.ml2wf.contract.business;

import com.ml2wf.contract.storage.graph.converter.IGraphStandardKnowledgeConverter;
import com.ml2wf.contract.storage.graph.dto.*;
import com.ml2wf.contract.storage.graph.repository.ConfigurationFeaturesRepository;
import com.ml2wf.contract.storage.graph.repository.ConfigurationRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractConfigurationsComponent<C extends GraphConfiguration<T, V, F>, T extends GraphStandardKnowledgeTask<T, V>,
        V extends GraphTaskVersion, F extends GraphConfigurationFeature<T, V>, O extends GraphConstraintOperand<T, V, O>>
        implements IConfigurationComponent {

    protected final ConfigurationRepository<C, T, V, F, String> configurationRepository;
    protected final ConfigurationFeaturesRepository<F, T, V, Long> configurationFeaturesRepository;
    protected final AbstractStandardKnowledgeComponent<T, O, V> standardKnowledgeComponent;
    protected final IVersionsComponent<V> versionsComponent;
    protected final IGraphStandardKnowledgeConverter<T, V> standardKnowledgeConverter;

    protected AbstractConfigurationsComponent(ConfigurationRepository<C, T, V, F, String> configurationRepository,
                                              ConfigurationFeaturesRepository<F, T, V, Long> configurationFeaturesRepository,
                                              AbstractStandardKnowledgeComponent<T, O, V> standardKnowledgeComponent,
                                              IVersionsComponent<V> versionsComponent,
                                              IGraphStandardKnowledgeConverter<T, V> standardKnowledgeConverter) {
        this.configurationRepository = configurationRepository;
        this.configurationFeaturesRepository = configurationFeaturesRepository;
        this.standardKnowledgeComponent = standardKnowledgeComponent;
        this.versionsComponent = versionsComponent;
        this.standardKnowledgeConverter = standardKnowledgeConverter;
    }
}
