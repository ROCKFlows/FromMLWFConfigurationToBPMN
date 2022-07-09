package com.ml2wf.arango.business.components;

import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.arango.storage.repository.ArangoVersionsRepository;
import com.ml2wf.contract.business.AbstractVersionsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("arango")
@Component
public class ArangoVersionsComponent extends AbstractVersionsComponent<ArangoTaskVersion> {

    public ArangoVersionsComponent(@Autowired ArangoVersionsRepository versionsRepository) {
        super(versionsRepository);
    }
}
