package com.ml2wf.contract.business;

import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.contract.storage.graph.repository.VersionsRepository;

import java.util.Optional;

public abstract class AbstractVersionsComponent<V extends GraphTaskVersion> implements IVersionsComponent<V> {

    private final VersionsRepository<V, String> versionsRepository;

    protected AbstractVersionsComponent(VersionsRepository<V, String> versionsRepository) {
        this.versionsRepository = versionsRepository;
    }

    @Override
    public Optional<V> getLastVersion() {
        return versionsRepository.getLastVersion();
    }
}
