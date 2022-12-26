package com.ml2wf.arango.storage.converter;

import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.contract.storage.graph.converter.IGraphStandardKnowledgeConverter;
import org.springframework.stereotype.Component;

@Component
public interface IArangoStandardKnowledgeConverter extends IGraphStandardKnowledgeConverter<ArangoTaskVersion> {

}
