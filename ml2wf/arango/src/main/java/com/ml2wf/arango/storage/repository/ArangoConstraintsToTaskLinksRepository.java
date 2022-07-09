package com.ml2wf.arango.storage.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.arango.storage.dto.ArangoConstraintToTaskLink;
import org.springframework.stereotype.Repository;

@Repository
public interface ArangoConstraintsToTaskLinksRepository extends ArangoRepository<ArangoConstraintToTaskLink, String> {

}
