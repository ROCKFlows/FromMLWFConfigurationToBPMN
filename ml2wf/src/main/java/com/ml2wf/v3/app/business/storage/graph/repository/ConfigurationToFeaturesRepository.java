package com.ml2wf.v3.app.business.storage.graph.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoConfigurationFeatureToTaskLink;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoConfigurationToFeatureLink;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationToFeaturesRepository extends ArangoRepository<ArangoConfigurationToFeatureLink, String> {

}
