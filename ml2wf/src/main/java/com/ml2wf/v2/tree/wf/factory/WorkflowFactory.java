package com.ml2wf.v2.tree.wf.factory;

import com.ml2wf.v2.tree.wf.Workflow;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/**
 * {@link Workflow}'s factory.
 *
 * @see Workflow
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkflowFactory {

    public static Workflow createWorkflow() {
        return new Workflow(new ArrayList<>());
    }
}
