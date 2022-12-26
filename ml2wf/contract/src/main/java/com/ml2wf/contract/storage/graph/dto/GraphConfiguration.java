package com.ml2wf.contract.storage.graph.dto;

import java.util.List;

public interface GraphConfiguration<V extends GraphTaskVersion> {

    String getName();

    V getVersion();

    List<? extends GraphConfigurationFeature<V>> getFeatures();
}
