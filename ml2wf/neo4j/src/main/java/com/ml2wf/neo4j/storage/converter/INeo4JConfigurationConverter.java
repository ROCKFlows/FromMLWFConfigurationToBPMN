package com.ml2wf.neo4j.storage.converter;

import com.ml2wf.contract.storage.graph.converter.IGraphConfigurationConverter;
import com.ml2wf.neo4j.storage.dto.Neo4JConfiguration;
import com.ml2wf.neo4j.storage.dto.Neo4JConfigurationFeature;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import org.springframework.stereotype.Component;

@Component
public interface INeo4JConfigurationConverter extends IGraphConfigurationConverter<Neo4JConfiguration,
        Neo4JConfigurationFeature, Neo4JStandardKnowledgeTask, Neo4JTaskVersion> {

}
