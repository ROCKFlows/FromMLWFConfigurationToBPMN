package com.ml2wf.neo4j.storage.repository;

import com.ml2wf.contract.storage.graph.repository.ConfigurationFeaturesRepository;
import com.ml2wf.neo4j.storage.dto.Neo4JConfigurationFeature;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Neo4JConfigurationFeaturesRepository extends Neo4jRepository<Neo4JConfigurationFeature, Long>,
        ConfigurationFeaturesRepository<Neo4JConfigurationFeature, Neo4JTaskVersion, Long> {

}
