package com.ml2wf.neo4j.business.components;

import com.ml2wf.contract.business.AbstractVersionsComponent;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import com.ml2wf.neo4j.storage.repository.Neo4JVersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("neo4j")
@Component
public class Neo4JVersionsComponent extends AbstractVersionsComponent<Neo4JTaskVersion> {

    public Neo4JVersionsComponent(@Autowired Neo4JVersionsRepository versionsRepository) {
        super(versionsRepository);
    }
}

