package com.ml2wf.arango.storage.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.arango.storage.dto.ArangoConfigurationToFeatureLink;
import org.springframework.stereotype.Repository;

@Repository
public interface ArangoConfigurationToFeaturesLinksRepository extends ArangoRepository<ArangoConfigurationToFeatureLink, String> {

}
