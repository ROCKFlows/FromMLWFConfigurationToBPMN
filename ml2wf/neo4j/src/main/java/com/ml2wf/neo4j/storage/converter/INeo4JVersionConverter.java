package com.ml2wf.neo4j.storage.converter;

import com.ml2wf.contract.storage.graph.converter.IGraphVersionConverter;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import org.springframework.stereotype.Component;

@Component
public interface INeo4JVersionConverter extends IGraphVersionConverter<Neo4JTaskVersion> {

}
