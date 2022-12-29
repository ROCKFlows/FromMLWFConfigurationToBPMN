package com.ml2wf.arango.storage.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.contract.storage.graph.dto.GraphConstraintOperand;
import com.ml2wf.contract.storage.graph.repository.ConstraintsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArangoConstraintsRepository extends ArangoRepository<GraphConstraintOperand<ArangoTaskVersion>, String>,
        ConstraintsRepository<ArangoTaskVersion, String> {

    List<GraphConstraintOperand<ArangoTaskVersion>> findAllByTypeEqualsAndVersion_Name(String name, String versionName);
}
