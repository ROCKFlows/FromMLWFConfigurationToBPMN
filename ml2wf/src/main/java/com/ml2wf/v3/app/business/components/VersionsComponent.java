package com.ml2wf.v3.app.business.components;

import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphTaskVersion;
import com.ml2wf.v3.app.business.storage.graph.contracts.repository.VersionsRepository;

import java.util.Optional;

public class VersionsComponent<V extends GraphTaskVersion> implements IVersionsComponent<V> {

    private final VersionsRepository<V, String> versionsRepository;

    public VersionsComponent(VersionsRepository<V, String> versionsRepository) {
        this.versionsRepository = versionsRepository;
    }

    @Override
    public Optional<V> getLastVersion() {
        return versionsRepository.getLastVersion();
    }
}
