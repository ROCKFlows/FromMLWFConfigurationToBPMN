package com.ml2wf.contract.storage.graph.dto;

import com.ml2wf.contract.storage.graph.Versioned;

import java.util.Collection;

public interface GraphStandardKnowledgeTask<V extends GraphTaskVersion> extends Versioned<V> {

    String getName();

    boolean isAbstract();

    boolean isMandatory();

    String getDescription();

    Collection<GraphStandardKnowledgeTask<V>> getChildren();
}
