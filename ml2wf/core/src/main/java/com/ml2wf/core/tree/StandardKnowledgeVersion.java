package com.ml2wf.core.tree;

import lombok.Value;

@Value
public class StandardKnowledgeVersion {

    int major;

    int minor;

    int patch;

    String name;
}
