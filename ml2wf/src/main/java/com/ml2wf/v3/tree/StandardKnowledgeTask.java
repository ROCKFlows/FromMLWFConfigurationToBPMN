package com.ml2wf.v3.tree;

import com.ml2wf.v3.INamedElement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class StandardKnowledgeTask implements ITree, INamedElement {

    private String name;
    private String documentation;
    private boolean isAbstract;
    private boolean isOptional;
    private String version;
    private List<StandardKnowledgeTask> tasks;

    @Override
    public String getName() {
        return name;
    }
}
