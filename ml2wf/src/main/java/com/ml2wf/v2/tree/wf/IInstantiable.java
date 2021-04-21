package com.ml2wf.v2.tree.wf;

/**
 * This {@link FunctionalInterface} defines the main method for instantiating
 * an object.
 *
 * @since 1.1
 */
@FunctionalInterface
public interface IInstantiable {

    /**
     * Instantiates the current implementation.
     */
    void instantiate();
}
