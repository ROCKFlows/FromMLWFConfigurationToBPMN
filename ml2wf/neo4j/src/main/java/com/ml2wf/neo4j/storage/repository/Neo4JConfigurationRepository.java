package com.ml2wf.neo4j.storage.repository;

import com.ml2wf.contract.storage.graph.repository.ConfigurationRepository;
import com.ml2wf.neo4j.storage.dto.Neo4JConfiguration;
import com.ml2wf.neo4j.storage.dto.Neo4JConfigurationFeature;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Neo4JConfigurationRepository extends Neo4jRepository<Neo4JConfiguration, String>,
        ConfigurationRepository<Neo4JConfiguration, Neo4JStandardKnowledgeTask, Neo4JTaskVersion, Neo4JConfigurationFeature, String> {

}
