package com.ml2wf.v3.app.business.storage.graph.neo4j.repository;

import com.ml2wf.v3.app.business.storage.graph.contracts.repository.ConfigurationFeaturesRepository;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JConfigurationFeature;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JTaskVersion;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Neo4JConfigurationFeaturesRepository extends Neo4jRepository<Neo4JConfigurationFeature, Long>,
        ConfigurationFeaturesRepository<Neo4JConfigurationFeature, Neo4JStandardKnowledgeTask, Neo4JTaskVersion, Long> {

}
