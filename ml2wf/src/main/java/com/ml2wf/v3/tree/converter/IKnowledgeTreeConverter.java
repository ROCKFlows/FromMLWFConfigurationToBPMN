package com.ml2wf.v3.tree.converter;

import com.ml2wf.v3.tree.StandardKnowledgeTree;
import com.ml2wf.v3.tree.custom.CustomKnowledgeTree;

public interface IKnowledgeTreeConverter<T extends CustomKnowledgeTree> {

    T fromStandardKnowledgeTree(final StandardKnowledgeTree standardKnowledgeTree);

    StandardKnowledgeTree toStandardKnowledgeTree(final T customTree);
}
