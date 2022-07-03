package com.ml2wf.v3.app.business.storage.graph.contracts.dto;

import java.util.Collection;

public interface GraphConstraintOperand<T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion,
        C extends GraphConstraintOperand<T, V, C>> {

    String getType();

    V getVersion();

    Collection<C> getOperands();

    T getTask();
}
