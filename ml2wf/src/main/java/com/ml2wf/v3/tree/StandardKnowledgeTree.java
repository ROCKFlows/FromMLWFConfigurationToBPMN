package com.ml2wf.v3.tree;

import com.ml2wf.v3.constraints.ConstraintTree;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class StandardKnowledgeTree implements ITree {

    private List<StandardKnowledgeTask> tasks;
    private List<ConstraintTree> constraints; // TODO: rename ContraintTree -> Constraint
}
