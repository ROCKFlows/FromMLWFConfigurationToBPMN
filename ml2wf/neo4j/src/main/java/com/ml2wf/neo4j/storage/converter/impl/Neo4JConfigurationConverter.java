package com.ml2wf.neo4j.storage.converter.impl;

import com.ml2wf.core.configurations.NamedConfiguration;
import com.ml2wf.neo4j.storage.converter.INeo4JConfigurationConverter;
import com.ml2wf.neo4j.storage.dto.Neo4JConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class Neo4JConfigurationConverter implements INeo4JConfigurationConverter {

    private final Neo4JConfigurationFeatureConverter configurationFeatureConverter;

    public Neo4JConfigurationConverter(@Autowired Neo4JConfigurationFeatureConverter configurationFeatureConverter) {
        this.configurationFeatureConverter = configurationFeatureConverter;
    }

    @Override
    public NamedConfiguration toStandardConfiguration(Neo4JConfiguration graphConfiguration) {
        return new NamedConfiguration(graphConfiguration.getName(), graphConfiguration.getFeatures().stream()
                .map(configurationFeatureConverter::toStandardConfigurationFeature)
                .collect(Collectors.toList())
        );
    }

    @Override
    public Neo4JConfiguration fromStandardConfiguration(NamedConfiguration configuration) {
        return new Neo4JConfiguration(
                configuration.getName(),
                null, // TODO: take source task version in consideration
                configuration.getFeatures().stream()
                    .map(configurationFeatureConverter::fromStandardConfigurationFeature)
                    .collect(Collectors.toList())
        );
    }
}
