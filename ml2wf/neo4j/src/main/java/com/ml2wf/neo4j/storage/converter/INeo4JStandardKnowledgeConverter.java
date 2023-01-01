package com.ml2wf.neo4j.storage.converter;

import com.ml2wf.contract.storage.graph.converter.IGraphStandardKnowledgeConverter;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import org.springframework.stereotype.Component;

@Component
public interface INeo4JStandardKnowledgeConverter extends IGraphStandardKnowledgeConverter<Neo4JStandardKnowledgeTask, Neo4JTaskVersion> {

}
