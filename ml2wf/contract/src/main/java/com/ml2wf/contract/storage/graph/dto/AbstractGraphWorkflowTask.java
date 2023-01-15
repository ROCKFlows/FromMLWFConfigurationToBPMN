package com.ml2wf.contract.storage.graph.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractGraphWorkflowTask<T extends AbstractGraphWorkflowTask<T, V>, V extends GraphTaskVersion>
        implements GraphStandardWorkflowTask<T, V> {


    protected String name;
    protected boolean isAbstract;
    protected boolean isOptional;
    protected String description;
}
