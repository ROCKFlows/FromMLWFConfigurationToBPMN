package com.ml2wf.contract.storage.graph.converter;

import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.core.tree.StandardKnowledgeVersion;

public interface IGraphVersionConverter<V extends GraphTaskVersion> {

    default StandardKnowledgeVersion toStandardKnowledgeVersion(V graphTaskVersion) {
        return new StandardKnowledgeVersion(
                graphTaskVersion.getMajor(),
                graphTaskVersion.getMinor(),
                graphTaskVersion.getPatch(),
                graphTaskVersion.getName()
        );
    }

    V fromStandardKnowledgeVersion(StandardKnowledgeVersion standardKnowledgeVersion);
}
