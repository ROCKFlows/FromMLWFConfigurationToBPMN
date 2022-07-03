package com.ml2wf.v3.app.business.storage.graph.neo4j.converter;

import com.ml2wf.v3.app.business.storage.graph.contracts.converter.IGraphStandardKnowledgeConverter;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JTaskVersion;
import org.springframework.stereotype.Component;

@Component
public interface INeo4JStandardKnowledgeConverter extends IGraphStandardKnowledgeConverter<Neo4JStandardKnowledgeTask, Neo4JTaskVersion>  {

}
