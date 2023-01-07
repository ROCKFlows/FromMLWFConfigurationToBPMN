package com.ml2wf.contract.business;

import com.ml2wf.core.tree.StandardKnowledgeVersion;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IVersionsComponent {

    StandardKnowledgeVersion getLastVersion();

    List<StandardKnowledgeVersion> getVersions();
}
