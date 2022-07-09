package com.ml2wf.contract.storage.graph.dto;

public interface GraphConfigurationFeature<T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion> {

    String getAutomatic();

    String getManual();

    T getTask();

    V getVersion();
}
