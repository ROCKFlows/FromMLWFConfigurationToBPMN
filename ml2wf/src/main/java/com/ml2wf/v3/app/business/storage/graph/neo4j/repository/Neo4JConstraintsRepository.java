package com.ml2wf.v3.app.business.storage.graph.neo4j.repository;

import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JConstraintOperand;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Neo4JConstraintsRepository extends Neo4jRepository<Neo4JConstraintOperand, Long> {

    List<Neo4JConstraintOperand> findAllByTypeEqualsAndVersion_Name(String name, String versionName); // TODO
}
