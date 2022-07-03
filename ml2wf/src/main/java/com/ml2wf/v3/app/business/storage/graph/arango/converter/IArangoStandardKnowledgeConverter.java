package com.ml2wf.v3.app.business.storage.graph.arango.converter;

import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoTaskVersion;
import com.ml2wf.v3.app.business.storage.graph.contracts.converter.IGraphStandardKnowledgeConverter;
import com.ml2wf.v3.app.constraints.ConstraintTree;
import com.ml2wf.v3.app.tree.StandardKnowledgeTask;
import com.ml2wf.v3.app.tree.StandardKnowledgeTree;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IArangoStandardKnowledgeConverter extends IGraphStandardKnowledgeConverter<ArangoStandardKnowledgeTask, ArangoTaskVersion> {

}
