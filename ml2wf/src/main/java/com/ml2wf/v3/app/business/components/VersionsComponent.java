package com.ml2wf.v3.app.business.components;

import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoTaskVersion;
import com.ml2wf.v3.app.business.storage.graph.arango.repository.ArangoVersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VersionsComponent {

    private final ArangoVersionsRepository versionsRepository;

    public VersionsComponent(@Autowired ArangoVersionsRepository versionsRepository) {
        this.versionsRepository = versionsRepository;
    }

    public Optional<ArangoTaskVersion> getLastVersion() {
        return versionsRepository.getLastVersion();
    }
}
