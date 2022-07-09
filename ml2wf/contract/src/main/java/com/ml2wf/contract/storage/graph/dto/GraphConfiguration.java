package com.ml2wf.contract.storage.graph.dto;

import java.util.List;

public interface GraphConfiguration<T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion,
        F extends GraphConfigurationFeature<T, V>> {

    String getName();

    V getVersion();

    List<F> getFeatures();
}
