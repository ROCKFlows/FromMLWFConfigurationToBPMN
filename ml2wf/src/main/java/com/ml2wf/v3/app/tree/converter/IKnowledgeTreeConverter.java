package com.ml2wf.v3.app.tree.converter;

import com.ml2wf.v3.app.tree.custom.CustomKnowledgeTree;
import com.ml2wf.v3.app.tree.StandardKnowledgeTree;

public interface IKnowledgeTreeConverter<T extends CustomKnowledgeTree> {

    T fromStandardKnowledgeTree(final StandardKnowledgeTree standardKnowledgeTree);

    StandardKnowledgeTree toStandardKnowledgeTree(final T customTree);
}
