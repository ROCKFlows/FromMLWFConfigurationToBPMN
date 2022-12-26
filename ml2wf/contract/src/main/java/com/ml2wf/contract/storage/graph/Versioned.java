package com.ml2wf.contract.storage.graph;

import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;

public interface Versioned<V extends GraphTaskVersion> {

    V getVersion();

    void setVersion(V version);
}
