package com.ml2wf.contract.storage.graph.dto;

import com.ml2wf.contract.storage.graph.Versioned;

import java.util.Collection;

public interface GraphConstraintOperand<O extends GraphConstraintOperand<O, T, V>,
        T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion> extends Versioned<V> {

    String getType();

    Collection<O> getOperands();

    T getTask();
}
