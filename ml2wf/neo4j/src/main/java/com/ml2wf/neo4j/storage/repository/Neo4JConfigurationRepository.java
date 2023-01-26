package com.ml2wf.neo4j.storage.repository;

import com.ml2wf.contract.storage.graph.repository.ConfigurationRepository;
import com.ml2wf.neo4j.storage.dto.Neo4JConfiguration;
import com.ml2wf.neo4j.storage.dto.Neo4JConfigurationFeature;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Neo4JConfigurationRepository extends Neo4jRepository<Neo4JConfiguration, String>,
        ConfigurationRepository<Neo4JConfiguration, Neo4JConfigurationFeature, Neo4JStandardKnowledgeTask,
                Neo4JTaskVersion, String> {

    @Override
    @Query("""
            MATCH (r:Configuration {name: $configurationName})<-[:IS_PARENT_OF*0..]-(:Configuration {name: '__ROOT_CONFIGURATION'})-[:IS_VERSIONED_BY*]->({name: $versionName}), f = (c:Configuration)<-[:IS_PARENT_OF*0..]-(r)
            RETURN r, collect(nodes(f)), collect(relationships(f))
            """)
    Optional<Neo4JConfiguration> findOneByNameAndVersionName(String configurationName, String versionName);
}
