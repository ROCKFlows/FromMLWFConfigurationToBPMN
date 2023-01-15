package com.ml2wf.neo4j.storage.converter;

import com.ml2wf.contract.storage.graph.converter.IGraphStandardWorkflowConverter;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardWorkflowTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import org.springframework.stereotype.Component;

@Component
public interface INeo4JStandardWorkflowConverter extends IGraphStandardWorkflowConverter<Neo4JStandardWorkflowTask, Neo4JTaskVersion> {

}
