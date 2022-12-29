package com.ml2wf.contract.storage.graph.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractGraphKnowledgeTask<T extends GraphTaskVersion> implements GraphStandardKnowledgeTask<T> {

    protected String name;
    protected boolean isAbstract;
    protected boolean isMandatory;
    protected String description;
}
