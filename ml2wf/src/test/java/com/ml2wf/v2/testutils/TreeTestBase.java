package com.ml2wf.v2.testutils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class TreeTestBase<F> {

    /**
     * {@code ClassLoader}'s instance used to get resources.
     *
     * @see ClassLoader
     */
    protected static ClassLoader classLoader = TreeTestBase.class.getClassLoader();

    protected final F factory;
}
