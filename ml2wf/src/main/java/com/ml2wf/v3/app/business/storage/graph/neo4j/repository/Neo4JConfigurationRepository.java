package com.ml2wf.v3.app.business.storage.graph.neo4j.repository;

import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JConfiguration;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Neo4JConfigurationRepository extends Neo4jRepository<Neo4JConfiguration, String> {

}
