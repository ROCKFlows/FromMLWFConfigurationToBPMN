package com.ml2wf.v3.app.business.storage.graph.arango.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoConfiguration;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends ArangoRepository<ArangoConfiguration, String> {

}
