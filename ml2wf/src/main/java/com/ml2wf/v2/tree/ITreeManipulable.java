package com.ml2wf.v2.tree;

import io.vavr.control.Either;
import lombok.NonNull;

import java.util.Optional;

/**
 * This interface defines the main method for tree manipulation like child
 * addition or removal.
 *
 * @param <T> the type of the tree elements
 *
 * @see ITreeIterator
 * @see Identifiable
 * @see INormalizable
 *
 * @since 1.1.0
 */
public interface ITreeManipulable<T extends Identifiable<I>, I> extends INormalizable {

    /**
     * Returns an {@link ITreeIterator} for iterating over the tree's children.
     *
     * @return an {@link ITreeIterator} for iterating over the tree's children
     *
     * @see ITreeIterator
     */
    ITreeIterator<T> iterator();

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
     * @return an {@link Either} instance containing the appended child if success else the error message
     */
    Either<String, T> appendChild(T child);

    /**
     * Removes the given child from the current tree implementation.
     *
     * @param child the child
     *
     * @return an {@link Optional} containing the removed child
     */
    Optional<T> removeChild(T child);

    /**
     * Retrieves the node with the given identity.
     *
     * @param identity  the identity of the requested node
     *
     * @return the retrieved node
     */
    Optional<T> getChildWithIdentity(@NonNull I identity);
}
