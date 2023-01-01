package com.ml2wf.contract.storage.graph.dto;

import com.ml2wf.contract.storage.graph.Versioned;

import java.util.Collection;

public interface GraphStandardKnowledgeTask<T extends GraphStandardKnowledgeTask<T, V>,
        V extends GraphTaskVersion> extends Versioned<V> {

    String getName();

    boolean isAbstract();

    boolean isMandatory();

    String getDescription();

    Collection<T> getChildren();
}
