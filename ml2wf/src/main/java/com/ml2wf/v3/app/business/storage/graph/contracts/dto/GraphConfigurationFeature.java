package com.ml2wf.v3.app.business.storage.graph.contracts.dto;

public interface GraphConfigurationFeature<T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion> {

    String getAutomatic();

    String getManual();

    T getTask();

    V getVersion();
}
