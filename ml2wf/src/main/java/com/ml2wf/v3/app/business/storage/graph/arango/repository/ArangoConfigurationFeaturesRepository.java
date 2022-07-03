package com.ml2wf.v3.app.business.storage.graph.arango.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoConfigurationFeature;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoTaskVersion;
import com.ml2wf.v3.app.business.storage.graph.contracts.repository.ConfigurationFeaturesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArangoConfigurationFeaturesRepository extends ArangoRepository<ArangoConfigurationFeature, String>,
        ConfigurationFeaturesRepository<ArangoConfigurationFeature, ArangoStandardKnowledgeTask, ArangoTaskVersion, String> {

}
