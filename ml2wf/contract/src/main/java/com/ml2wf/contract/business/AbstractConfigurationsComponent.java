package com.ml2wf.contract.business;

import com.ml2wf.contract.storage.graph.converter.IGraphStandardKnowledgeConverter;
import com.ml2wf.contract.storage.graph.dto.*;
import com.ml2wf.contract.storage.graph.repository.ConfigurationFeaturesRepository;
import com.ml2wf.contract.storage.graph.repository.ConfigurationRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractConfigurationsComponent<
        C extends GraphConfiguration<V>,
        T extends GraphStandardKnowledgeTask<V>,
        V extends GraphTaskVersion,
        F extends GraphConfigurationFeature<V>,
        O extends GraphConstraintOperand<V>
        >
        implements IConfigurationComponent {

    protected final ConfigurationRepository<C, V, String> configurationRepository;
    protected final ConfigurationFeaturesRepository<F, V, Long> configurationFeaturesRepository;
    protected final AbstractStandardKnowledgeComponent<T, O, V> standardKnowledgeComponent;
    protected final IVersionsComponent<V> versionsComponent;
    protected final IGraphStandardKnowledgeConverter<V> standardKnowledgeConverter;

    protected AbstractConfigurationsComponent(ConfigurationRepository<C, V, String> configurationRepository,
                                              ConfigurationFeaturesRepository<F, V, Long> configurationFeaturesRepository,
                                              AbstractStandardKnowledgeComponent<T, O, V> standardKnowledgeComponent,
                                              IVersionsComponent<V> versionsComponent,
                                              IGraphStandardKnowledgeConverter<V> standardKnowledgeConverter) {
        this.configurationRepository = configurationRepository;
        this.configurationFeaturesRepository = configurationFeaturesRepository;
        this.standardKnowledgeComponent = standardKnowledgeComponent;
        this.versionsComponent = versionsComponent;
        this.standardKnowledgeConverter = standardKnowledgeConverter;
    }
}
