package com.ml2wf.v3.app.business.components.arango;

import com.ml2wf.v3.app.business.components.VersionsComponent;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoTaskVersion;
import com.ml2wf.v3.app.business.storage.graph.arango.repository.ArangoVersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("arango")
@Component
public class ArangoVersionsComponent extends VersionsComponent<ArangoTaskVersion> {

    public ArangoVersionsComponent(@Autowired ArangoVersionsRepository versionsRepository) {
        super(versionsRepository);
    }
}
