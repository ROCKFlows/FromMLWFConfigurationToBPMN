package com.ml2wf.v3.app.business.storage.graph.contracts.dto;

import java.util.List;

public interface GraphConfiguration<T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion,
        F extends GraphConfigurationFeature<T, V>> {

    String getName();

    V getVersion();

    List<F> getFeatures();
}
