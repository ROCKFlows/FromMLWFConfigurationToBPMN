package com.ml2wf.v3.app.business.storage.graph.neo4j.converter;

import com.ml2wf.v3.app.business.storage.graph.contracts.converter.IGraphConstraintsConverter;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JConstraintOperand;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JTaskVersion;
import org.springframework.stereotype.Component;

@Component
public interface INeo4JConstraintsConverter extends IGraphConstraintsConverter<Neo4JStandardKnowledgeTask, Neo4JConstraintOperand, Neo4JTaskVersion> {

}
