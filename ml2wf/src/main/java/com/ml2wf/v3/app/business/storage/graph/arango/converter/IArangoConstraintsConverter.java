package com.ml2wf.v3.app.business.storage.graph.arango.converter;

import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoConstraintOperand;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoTaskVersion;
import com.ml2wf.v3.app.business.storage.graph.contracts.converter.IGraphConstraintsConverter;
import org.springframework.stereotype.Component;

@Component
public interface IArangoConstraintsConverter extends IGraphConstraintsConverter<ArangoStandardKnowledgeTask,
        ArangoConstraintOperand, ArangoTaskVersion> {

}
