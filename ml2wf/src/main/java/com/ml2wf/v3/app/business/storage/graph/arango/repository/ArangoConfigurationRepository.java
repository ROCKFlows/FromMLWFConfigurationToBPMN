package com.ml2wf.v3.app.business.storage.graph.arango.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoConfiguration;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoConfigurationFeature;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoTaskVersion;
import com.ml2wf.v3.app.business.storage.graph.contracts.repository.ConfigurationRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArangoConfigurationRepository extends ArangoRepository<ArangoConfiguration, String>,
        ConfigurationRepository<ArangoConfiguration, ArangoStandardKnowledgeTask, ArangoTaskVersion, ArangoConfigurationFeature, String> {

}
