package com.ml2wf.arango.storage.converter.impl;

import com.ml2wf.arango.storage.converter.IArangoVersionConverter;
import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.core.tree.StandardKnowledgeVersion;
import org.springframework.stereotype.Component;

@Component
public class ArangoVersionConverter implements IArangoVersionConverter {

    @Override
    public ArangoTaskVersion fromStandardKnowledgeVersion(StandardKnowledgeVersion standardKnowledgeVersion) {
        return new ArangoTaskVersion(
                standardKnowledgeVersion.getMajor(),
                standardKnowledgeVersion.getMinor(),
                standardKnowledgeVersion.getPatch(),
                standardKnowledgeVersion.getName()
        );
    }
}
