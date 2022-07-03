package com.ml2wf.v3.app.business.components;

import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphTaskVersion;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface IVersionsComponent<V extends GraphTaskVersion> {

    Optional<V> getLastVersion();
}
