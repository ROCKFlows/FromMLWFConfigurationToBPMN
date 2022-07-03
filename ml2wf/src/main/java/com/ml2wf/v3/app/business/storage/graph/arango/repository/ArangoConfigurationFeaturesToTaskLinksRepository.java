package com.ml2wf.v3.app.business.storage.graph.arango.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoConfigurationFeatureToTaskLink;
import org.springframework.stereotype.Repository;

@Repository
public interface ArangoConfigurationFeaturesToTaskLinksRepository extends ArangoRepository<ArangoConfigurationFeatureToTaskLink, String> {

}
