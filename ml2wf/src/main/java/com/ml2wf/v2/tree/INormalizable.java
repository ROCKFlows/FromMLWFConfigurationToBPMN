package com.ml2wf.v2.tree;

/**
 * This {@link FunctionalInterface} defines the main method for normalizing
 * an object.
 *
 * @since 1.1
 */
@FunctionalInterface
public interface INormalizable {

    /**
     * Normalizes the current element.
     */
    void normalize();
}
