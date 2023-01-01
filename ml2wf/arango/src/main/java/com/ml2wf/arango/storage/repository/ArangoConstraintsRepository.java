package com.ml2wf.arango.storage.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.arango.storage.dto.ArangoConstraintOperand;
import com.ml2wf.arango.storage.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.contract.storage.graph.dto.GraphConstraintOperand;
import com.ml2wf.contract.storage.graph.repository.ConstraintsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArangoConstraintsRepository extends ArangoRepository<ArangoConstraintOperand, String>,
        ConstraintsRepository<ArangoConstraintOperand, ArangoStandardKnowledgeTask, ArangoTaskVersion, String> {

    List<ArangoConstraintOperand> findAllByTypeEqualsAndVersion_Name(String name, String versionName);
}
