package com.ml2wf.core.tree.converter;

import com.ml2wf.core.tree.StandardKnowledgeTask;
import com.ml2wf.core.tree.StandardKnowledgeTree;
import com.ml2wf.core.tree.custom.CustomKnowledgeTree;

public interface IKnowledgeTreeConverter<T extends CustomKnowledgeTree, S> { // TODO: define S

    T fromStandardKnowledgeTree(final StandardKnowledgeTree standardKnowledgeTree);

    StandardKnowledgeTree toStandardKnowledgeTree(final T customTree);

    S fromStandardKnowledgeTask(StandardKnowledgeTask standardKnowledgeTask);

    StandardKnowledgeTask toStandardKnowledgeTask(S featureModelTask);
}
