package com.ml2wf.arango.storage.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.arango.storage.dto.ArangoConfiguration;
import com.ml2wf.arango.storage.dto.ArangoConfigurationFeature;
import com.ml2wf.arango.storage.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.contract.storage.graph.repository.ConfigurationRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArangoConfigurationRepository extends ArangoRepository<ArangoConfiguration, String>,
        ConfigurationRepository<ArangoConfiguration, ArangoConfigurationFeature, ArangoStandardKnowledgeTask,
                ArangoTaskVersion, String> {

}
