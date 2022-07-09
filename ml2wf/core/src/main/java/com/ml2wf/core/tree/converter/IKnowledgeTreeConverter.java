package com.ml2wf.core.tree.converter;

import com.ml2wf.core.tree.StandardKnowledgeTree;
import com.ml2wf.core.tree.custom.CustomKnowledgeTree;

public interface IKnowledgeTreeConverter<T extends CustomKnowledgeTree> {

    T fromStandardKnowledgeTree(final StandardKnowledgeTree standardKnowledgeTree);

    StandardKnowledgeTree toStandardKnowledgeTree(final T customTree);
}
