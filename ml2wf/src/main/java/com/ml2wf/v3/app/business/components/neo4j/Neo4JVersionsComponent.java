package com.ml2wf.v3.app.business.components.neo4j;

import com.ml2wf.v3.app.business.components.VersionsComponent;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JTaskVersion;
import com.ml2wf.v3.app.business.storage.graph.neo4j.repository.Neo4JVersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("neo4j")
@Component
public class Neo4JVersionsComponent extends VersionsComponent<Neo4JTaskVersion> {

    public Neo4JVersionsComponent(@Autowired Neo4JVersionsRepository versionsRepository) {
        super(versionsRepository);
    }
}

