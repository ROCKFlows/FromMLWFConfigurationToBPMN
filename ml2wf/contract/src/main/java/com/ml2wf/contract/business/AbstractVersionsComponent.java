package com.ml2wf.contract.business;

import com.ml2wf.contract.exception.NoVersionFoundException;
import com.ml2wf.contract.storage.graph.converter.IGraphVersionConverter;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.contract.storage.graph.repository.VersionsRepository;
import com.ml2wf.core.tree.StandardKnowledgeVersion;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractVersionsComponent<V extends GraphTaskVersion> implements IVersionsComponent {

    private final VersionsRepository<V, String> versionsRepository;
    private final IGraphVersionConverter<V> graphVersionConverter;

    protected AbstractVersionsComponent(VersionsRepository<V, String> versionsRepository,
                                        IGraphVersionConverter<V> graphVersionConverter) {
        this.versionsRepository = versionsRepository;
        this.graphVersionConverter = graphVersionConverter;
    }

    @Override
    public StandardKnowledgeVersion getLastVersion() {
        return graphVersionConverter.toStandardKnowledgeVersion(
                versionsRepository.getLastVersion()
                        .orElseThrow(NoVersionFoundException::new)
        );
    }

    @Override
    public List<StandardKnowledgeVersion> getVersions() {
        return StreamSupport.stream(versionsRepository.findAll().spliterator(), false)
                .map(graphVersionConverter::toStandardKnowledgeVersion)
                .collect(Collectors.toList());
    }
}
