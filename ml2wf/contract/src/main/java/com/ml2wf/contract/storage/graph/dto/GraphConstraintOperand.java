package com.ml2wf.contract.storage.graph.dto;

import java.util.Collection;

public interface GraphConstraintOperand<V extends GraphTaskVersion> {

    String getType();

    V getVersion();

    void setVersion(V version);

    Collection<? extends GraphConstraintOperand<V>> getOperands();

    GraphStandardKnowledgeTask<V> getTask();
}
