package com.ml2wf.contract.storage.graph.converter;

import com.ml2wf.contract.storage.graph.dto.GraphConfigurationFeature;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.core.configurations.ConfigurationFeature;
import org.springframework.stereotype.Component;


@Component
public interface IGraphConfigurationFeatureConverter<F extends GraphConfigurationFeature<T, V>,
        T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion> {

    default ConfigurationFeature toStandardConfigurationFeature(F graphConfigurationFeature) {
        return new ConfigurationFeature(
                ConfigurationFeature.Status.valueOf(graphConfigurationFeature.getAutomatic()),
                ConfigurationFeature.Status.valueOf(graphConfigurationFeature.getManual()),
                graphConfigurationFeature.getTask().getName()
        );
    }
    F fromStandardConfigurationFeature(ConfigurationFeature configurationFeature);
}
