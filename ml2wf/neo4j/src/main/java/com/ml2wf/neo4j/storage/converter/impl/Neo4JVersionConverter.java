package com.ml2wf.neo4j.storage.converter.impl;

import com.ml2wf.core.tree.StandardKnowledgeVersion;
import com.ml2wf.neo4j.storage.converter.INeo4JVersionConverter;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import org.springframework.stereotype.Component;

@Component
public class Neo4JVersionConverter implements INeo4JVersionConverter {

    @Override
    public Neo4JTaskVersion fromStandardKnowledgeVersion(StandardKnowledgeVersion standardKnowledgeVersion) {
        return new Neo4JTaskVersion(
                standardKnowledgeVersion.getMajor(),
                standardKnowledgeVersion.getMinor(),
                standardKnowledgeVersion.getPatch(),
                standardKnowledgeVersion.getName(),
                null
        );
    }
}
