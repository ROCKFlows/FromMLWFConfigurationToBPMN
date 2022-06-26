package com.ml2wf.v3.app.workflow;

import com.ml2wf.v3.app.INamedElement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE) // required by Jackson
@Data
public class StandardWorkflowTask implements INamedElement {

    private String name;
    private String description;
    private boolean isAbstract;
    private boolean isOptional;

    @Override
    public String getName() {
        return name;
    }
}
