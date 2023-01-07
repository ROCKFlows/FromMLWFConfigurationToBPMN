package com.ml2wf.contract.business;

import com.ml2wf.contract.storage.graph.converter.IGraphStandardKnowledgeConverter;
import com.ml2wf.contract.storage.graph.dto.*;
import com.ml2wf.contract.storage.graph.repository.ConfigurationFeaturesRepository;
import com.ml2wf.contract.storage.graph.repository.ConfigurationRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractConfigurationsComponent<
        C extends GraphConfiguration<F, T, V>,
        V extends GraphTaskVersion,
        T extends GraphStandardKnowledgeTask<T, V>,
        O extends GraphConstraintOperand<O, T, V>,
        F extends GraphConfigurationFeature<T, V>
        >
        implements IConfigurationComponent {

    protected final ConfigurationRepository<C, F, T, V, String> configurationRepository;
    protected final ConfigurationFeaturesRepository<F, T, V, Long> configurationFeaturesRepository;
    protected final AbstractStandardKnowledgeComponent<T, O, V> standardKnowledgeComponent;
    protected final IVersionsComponent versionsComponent;
    protected final IGraphStandardKnowledgeConverter<T, V> standardKnowledgeConverter;

    protected AbstractConfigurationsComponent(ConfigurationRepository<C, F, T, V, String> configurationRepository,
                                              ConfigurationFeaturesRepository<F, T, V, Long> configurationFeaturesRepository,
                                              AbstractStandardKnowledgeComponent<T, O, V> standardKnowledgeComponent,
                                              IVersionsComponent versionsComponent,
                                              IGraphStandardKnowledgeConverter<T, V> standardKnowledgeConverter) {
        this.configurationRepository = configurationRepository;
        this.configurationFeaturesRepository = configurationFeaturesRepository;
        this.standardKnowledgeComponent = standardKnowledgeComponent;
        this.versionsComponent = versionsComponent;
        this.standardKnowledgeConverter = standardKnowledgeConverter;
    }
}
