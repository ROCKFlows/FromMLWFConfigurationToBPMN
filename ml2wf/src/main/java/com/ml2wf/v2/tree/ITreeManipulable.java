package com.ml2wf.v2.tree;

import java.util.Optional;

/**
 * This interface defines the main method for tree manipulation like child
 * addition or removal.
 *
 * @param <T> the type of the tree elements
 *
 * @since 1.1.0
 */
public interface ITreeManipulable<T> extends INormalizable {

    // TODO: should we define a method to move a child according to the TreeEvent#Events#MOVE event ?

    /**
     * Returns whether the current tree implementation has any child.
     *
     * @return whether the current tree implementation has any child
     */
    boolean hasChildren();

    /**
     * Appends the given child to the current tree implementation.
     *
     * @param child the child
     *
     * @return the appended child to allow chaining
     */
    T appendChild(T child);

    /**
     * Removes the given child from the current tree implementation.
     *
     * @param child the child
     *
     * @return an {@link Optional} containing the removed child
     */
    Optional<T> removeChild(T child);

    /**
     * Retrieves the node with the given name.
     *
     * @param name  the name of the requested node
     *
     * @return the retrieved node
     */
    Optional<T> getChildWithName(String name);
}
