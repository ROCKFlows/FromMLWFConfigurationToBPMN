package com.ml2wf.contract.storage.graph.dto;

import java.util.Collection;

public interface GraphStandardKnowledgeTask<V extends GraphTaskVersion> {

    String getName();

    boolean isAbstract();

    boolean isMandatory();

    V getVersion();

    void setVersion(V version);

    String getDescription();

    Collection<? extends GraphStandardKnowledgeTask<V>> getChildren();
}
