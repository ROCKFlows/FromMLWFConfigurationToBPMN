package com.ml2wf.contract.business;

import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface IVersionsComponent<V extends GraphTaskVersion> {

    Optional<V> getLastVersion();
}
