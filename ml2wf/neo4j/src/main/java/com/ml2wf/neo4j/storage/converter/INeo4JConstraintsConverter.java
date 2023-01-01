package com.ml2wf.neo4j.storage.converter;

import com.ml2wf.contract.storage.graph.converter.IGraphConstraintsConverter;
import com.ml2wf.neo4j.storage.dto.Neo4JConstraintOperand;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import org.springframework.stereotype.Component;

@Component
public interface INeo4JConstraintsConverter extends IGraphConstraintsConverter<Neo4JConstraintOperand,
        Neo4JStandardKnowledgeTask, Neo4JTaskVersion> {

}
