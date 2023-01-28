package com.ml2wf.contract.storage.graph.converter;

import com.ml2wf.contract.storage.graph.dto.GraphConfiguration;
import com.ml2wf.contract.storage.graph.dto.GraphConfigurationFeature;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.core.configurations.NamedConfiguration;
import org.springframework.stereotype.Component;

@Component
public interface IGraphConfigurationConverter<C extends GraphConfiguration<F, T, V>,
        F extends GraphConfigurationFeature<T, V>, T extends GraphStandardKnowledgeTask<T, V>,
        V extends GraphTaskVersion> {

    NamedConfiguration toStandardConfiguration(C graphConfiguration);

    C fromStandardConfiguration(NamedConfiguration configuration);
}
