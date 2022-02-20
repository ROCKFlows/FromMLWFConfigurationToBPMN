package com.ml2wf.v2.tree.wf;

/**
 * This {@link FunctionalInterface} defines the main method for instantiating
 * an object.
 *
 * @since 1.1.0
 */
@FunctionalInterface
public interface Instantiable {

    /**
     * Instantiates the current implementation.
     */
    void instantiate();
}
