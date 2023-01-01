package com.ml2wf.arango.storage.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.arango.storage.dto.ArangoConstraintOperand;
import com.ml2wf.arango.storage.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.contract.storage.graph.repository.ConstraintsRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArangoConstraintsRepository extends ArangoRepository<ArangoConstraintOperand, String>,
        ConstraintsRepository<ArangoConstraintOperand, ArangoStandardKnowledgeTask, ArangoTaskVersion, String> {

}
