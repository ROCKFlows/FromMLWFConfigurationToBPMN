package com.ml2wf.v2.tree;

/**
 * This interface defines the main method for tree manipulation like child
 * addition or removal.
 *
 * @param <T> the type of the tree elements
 *
 * @since 1.1
 */
public interface ITreeManipulable<T> extends INormalizable {

    /**
     * Appends the given child to the current tree implementation.
     *
     * @param child the child
     *
     * @return the appended child to allow chaining.
     */
    T appendChild(T child);

    /**
     * Removes the given child from the current tree implementation.
     *
     * @param child the child
     *
     * @return the removed child to allow chaining.
     */
    T removeChild(T child);
}
