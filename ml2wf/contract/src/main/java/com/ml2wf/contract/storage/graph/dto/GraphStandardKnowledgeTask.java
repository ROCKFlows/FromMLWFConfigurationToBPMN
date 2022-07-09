package com.ml2wf.contract.storage.graph.dto;

import java.util.Collection;

public interface GraphStandardKnowledgeTask<T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion> {

    String getName();

    boolean isAbstract();

    boolean isMandatory();

    V getVersion();

    String getDescription();

    Collection<T> getChildren();
}
