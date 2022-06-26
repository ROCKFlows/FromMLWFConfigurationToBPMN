package com.ml2wf.v3.app.business.storage.graph;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoConstraintOperand;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConstraintsRepository extends ArangoRepository<ArangoConstraintOperand, String> {

    List<ArangoConstraintOperand> findAllByTypeEqualsAndVersion_Name(String name, String versionName);
}
