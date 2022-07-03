package com.ml2wf.v3.app.business.storage.graph.arango.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.*;
import com.ml2wf.v3.app.business.storage.graph.contracts.repository.ConstraintsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArangoConstraintsRepository extends ArangoRepository<ArangoConstraintOperand, String>,
        ConstraintsRepository<ArangoConstraintOperand, ArangoStandardKnowledgeTask, ArangoTaskVersion, String> {

    List<ArangoConstraintOperand> findAllByTypeEqualsAndVersion_Name(String name, String versionName);
}
