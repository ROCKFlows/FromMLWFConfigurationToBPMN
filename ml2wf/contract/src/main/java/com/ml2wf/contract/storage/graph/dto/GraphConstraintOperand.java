package com.ml2wf.contract.storage.graph.dto;

import com.ml2wf.contract.storage.graph.Versioned;

import java.util.Collection;

public interface GraphConstraintOperand<V extends GraphTaskVersion> extends Versioned<V> {

    String getType();

    Collection<GraphConstraintOperand<V>> getOperands();

    GraphStandardKnowledgeTask<V> getTask();
}
