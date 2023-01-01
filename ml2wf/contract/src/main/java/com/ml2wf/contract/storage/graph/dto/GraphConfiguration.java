package com.ml2wf.contract.storage.graph.dto;

import java.util.List;

public interface GraphConfiguration<F extends GraphConfigurationFeature<T, V>,
        T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion> {

    String getName();

    V getVersion();

    List<F> getFeatures();
}
