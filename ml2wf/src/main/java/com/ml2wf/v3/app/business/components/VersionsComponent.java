package com.ml2wf.v3.app.business.components;

import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphTaskVersion;
import com.ml2wf.v3.app.business.storage.graph.contracts.repository.VersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VersionsComponent<V extends GraphTaskVersion> implements IVersionsComponent<V> {

    private final VersionsRepository<V, String> versionsRepository;

    public VersionsComponent(@Autowired VersionsRepository<V, String> versionsRepository) {
        this.versionsRepository = versionsRepository;
    }

    @Override
    public Optional<V> getLastVersion() {
        return versionsRepository.getLastVersion();
    }
}
