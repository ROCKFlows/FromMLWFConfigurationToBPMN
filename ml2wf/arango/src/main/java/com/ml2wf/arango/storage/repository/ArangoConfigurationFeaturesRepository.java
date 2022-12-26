package com.ml2wf.arango.storage.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.arango.storage.dto.ArangoConfigurationFeature;
import com.ml2wf.arango.storage.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.contract.storage.graph.repository.ConfigurationFeaturesRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArangoConfigurationFeaturesRepository extends ArangoRepository<ArangoConfigurationFeature, Long>,
        ConfigurationFeaturesRepository<ArangoConfigurationFeature, ArangoTaskVersion, Long> {

}
