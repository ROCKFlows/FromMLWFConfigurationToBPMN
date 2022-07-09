package com.ml2wf.arango.storage.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.arango.storage.dto.ArangoConstraintLink;
import org.springframework.stereotype.Repository;

@Repository
public interface ArangoConstraintsLinksRepository extends ArangoRepository<ArangoConstraintLink, String> {

}
