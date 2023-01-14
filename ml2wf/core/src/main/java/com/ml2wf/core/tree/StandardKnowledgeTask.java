package com.ml2wf.core.tree;

import com.ml2wf.core.INamedElement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class StandardKnowledgeTask implements ITree, INamedElement {

    private String name;
    private String documentation;
    private boolean isAbstract;
    private boolean isOptional;
    @Nullable
    private String version;
    private List<StandardKnowledgeTask> tasks;
}
