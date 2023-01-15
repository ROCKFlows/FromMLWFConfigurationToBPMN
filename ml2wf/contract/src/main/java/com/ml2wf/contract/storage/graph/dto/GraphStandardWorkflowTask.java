package com.ml2wf.contract.storage.graph.dto;

import com.ml2wf.contract.storage.graph.Versioned;

public interface GraphStandardWorkflowTask<T extends GraphStandardWorkflowTask<T, V>, V extends GraphTaskVersion>
        extends Versioned<V> {

    String getName();
    String getDescription();
    boolean isAbstract();
    boolean isOptional();
    T getNextTask();
    void setNextTask(T nextTask);
}
