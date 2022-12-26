package com.ml2wf.contract.storage.graph.dto;

public interface GraphConfigurationFeature<V extends GraphTaskVersion> {

    String getAutomatic();

    String getManual();

    GraphStandardKnowledgeTask<V> getTask();

    V getVersion();
}
