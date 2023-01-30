package com.ml2wf.graphql.controllers;

import com.ml2wf.contract.business.IVersionsComponent;
import com.ml2wf.core.tree.StandardKnowledgeVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class VersionGraphQLController {

    private final IVersionsComponent versionsComponent;

    VersionGraphQLController(@Autowired IVersionsComponent versionsComponent) {
        this.versionsComponent = versionsComponent;
    }

    @QueryMapping
    List<StandardKnowledgeVersion> versions() {
        return versionsComponent.getVersions();
    }

    @QueryMapping
    StandardKnowledgeVersion lastVersion() {
        return versionsComponent.getLastVersion();
    }
}
