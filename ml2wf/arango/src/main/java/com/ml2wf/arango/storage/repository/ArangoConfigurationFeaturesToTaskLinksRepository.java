package com.ml2wf.arango.storage.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.arango.storage.dto.ArangoConfigurationFeatureToTaskLink;
import org.springframework.stereotype.Repository;

@Repository
public interface ArangoConfigurationFeaturesToTaskLinksRepository extends ArangoRepository<ArangoConfigurationFeatureToTaskLink, String> {

}
