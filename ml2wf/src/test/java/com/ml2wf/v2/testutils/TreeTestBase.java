package com.ml2wf.v2.testutils;

import com.ml2wf.v2.tree.wf.factory.IWorkflowFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class TreeTestBase {

    /**
     * {@code ClassLoader}'s instance used to get resources.
     *
     * @see ClassLoader
     */
    protected static ClassLoader classLoader = TreeTestBase.class.getClassLoader();

    protected final IWorkflowFactory workflowFactory;
}
