package com.ml2wf.neo4j.storage.converter.impl;

import com.ml2wf.core.configurations.ConfigurationFeature;
import com.ml2wf.neo4j.storage.converter.INeo4JConfigurationFeatureConverter;
import com.ml2wf.neo4j.storage.dto.Neo4JConfigurationFeature;
import org.springframework.stereotype.Component;

@Component
public class Neo4JConfigurationFeatureConverter implements INeo4JConfigurationFeatureConverter {

    @Override
    public Neo4JConfigurationFeature fromStandardConfigurationFeature(ConfigurationFeature configurationFeature) {
        return new Neo4JConfigurationFeature(
                configurationFeature.getAutomatic().getLowercaseName(),
                configurationFeature.getManual().getLowercaseName(),
                null, // TODO: take source task version in consideration
                null); // TODO: take source task in consideration
    }
}
