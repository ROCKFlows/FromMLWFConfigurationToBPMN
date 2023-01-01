package com.ml2wf.arango.storage.converter;

import com.ml2wf.arango.storage.dto.ArangoConstraintOperand;
import com.ml2wf.arango.storage.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.contract.storage.graph.converter.IGraphConstraintsConverter;
import org.springframework.stereotype.Component;

@Component
public interface IArangoConstraintsConverter extends IGraphConstraintsConverter<ArangoConstraintOperand,
        ArangoStandardKnowledgeTask, ArangoTaskVersion> {

}
