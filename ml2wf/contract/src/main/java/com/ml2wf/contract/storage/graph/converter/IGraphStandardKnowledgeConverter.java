package com.ml2wf.contract.storage.graph.converter;

import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.core.constraints.ConstraintTree;
import com.ml2wf.core.tree.StandardKnowledgeTask;
import com.ml2wf.core.tree.StandardKnowledgeTree;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public interface IGraphStandardKnowledgeConverter<V extends GraphTaskVersion> {

    default StandardKnowledgeTree toStandardKnowledgeTree(GraphStandardKnowledgeTask<V> standardKnowledgeTask) {
        return new StandardKnowledgeTree(
                Collections.singletonList(toStandardKnowledgeTask(standardKnowledgeTask)),
                Collections.emptyList()
        );
    }

    default StandardKnowledgeTree toStandardKnowledgeTree(GraphStandardKnowledgeTask<V> standardKnowledgeTask, List<ConstraintTree> constraintTrees) {
        return new StandardKnowledgeTree(
                Collections.singletonList(toStandardKnowledgeTask(standardKnowledgeTask)),
                constraintTrees
        );
    }

    private String formatVersion(V version) {
        return String.format("v%d.%d.%d <%s>", version.getMajor(), version.getMinor(), version.getPatch(), version.getName());
    }

    default StandardKnowledgeTask toStandardKnowledgeTask(GraphStandardKnowledgeTask<V> standardKnowledgeTask) {
        return new StandardKnowledgeTask(
                standardKnowledgeTask.getName(),
                standardKnowledgeTask.getDescription(),
                standardKnowledgeTask.isAbstract(),
                !standardKnowledgeTask.isMandatory(),
                formatVersion(standardKnowledgeTask.getVersion()),
                standardKnowledgeTask.getChildren().stream()
                        .map(this::toStandardKnowledgeTask)
                        .collect(Collectors.toList())
        );
    }

    default List<GraphStandardKnowledgeTask<V>> fromStandardKnowledgeTree(StandardKnowledgeTree standardKnowledgeTree) {
        if (standardKnowledgeTree.getTasks().isEmpty()) {
            throw new IllegalStateException("Can't convert an empty knowledge tree model.");
        }
        return fromStandardKnowledgeTask(standardKnowledgeTree.getTasks().get(0)); // TODO: currently, we only support one root task
    }

    List<GraphStandardKnowledgeTask<V>> fromStandardKnowledgeTask(StandardKnowledgeTask standardKnowledgeTask);
}
