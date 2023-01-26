package com.ml2wf.contract.storage.graph.converter;

import com.ml2wf.contract.storage.graph.dto.GraphConfiguration;
import com.ml2wf.contract.storage.graph.dto.GraphConfigurationFeature;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.core.configurations.Configuration;
import org.springframework.stereotype.Component;

@Component
public interface IGraphConfigurationConverter<C extends GraphConfiguration<F, T, V>,
        F extends GraphConfigurationFeature<T, V>, T extends GraphStandardKnowledgeTask<T, V>,
        V extends GraphTaskVersion> {

    Configuration toStandardConfiguration(C graphConfiguration);

    C fromStandardConfiguration(String configurationName, Configuration configuration);
}
