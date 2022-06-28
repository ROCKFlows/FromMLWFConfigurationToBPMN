package com.ml2wf.v3.app.business.storage.graph.arango.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoConstraintToTaskLink;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstraintsToTaskLinksRepository extends ArangoRepository<ArangoConstraintToTaskLink, String> {

}
