package com.ml2wf.v2.tree;

import io.vavr.control.Either;
import lombok.NonNull;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This interface defines the main method for tree manipulation like child
 * addition or removal.
 *
 * @param <T> the type of the tree elements
 *
 * @see Identifiable
 * @see INormalizable
 *
 * @since 1.1.0
 */
public interface ITreeManipulable<T extends Identifiable<I>, I> extends INormalizable {

    /**
     * Returns a {@link Collection} containing the tree's children.
     *
     * @return a {@link Collection} containing the tree's children.
     */
    Collection<T> getChildren();

    /**
     * Returns a {@link Collection} containing the tree's children matching the given {@link Predicate}.
     *
     * <p>
     *
     * As opposed to {@link #appendDirectChild(Identifiable)} and {@link #removeDirectChild(Identifiable)},
     * this method will recursively search for the given match.
     *
     * @param predicate  the predicate to match
     *
     * @return a {@link Collection} containing the tree's children matching the given {@link Predicate}
     *
     * @see Predicate
     */
    Collection<T> getChildrenMatching(@NonNull Predicate<T> predicate);

    /**
     * Returns whether the current tree implementation has any child.
     *
     * @return whether the current tree implementation has any child
     */
    boolean hasChildren();

    /**
     * Appends the given child to the current tree's direct children.
     *
     * @param child the child
     *
     * @return an {@link Either} instance containing the appended child if success else the error message
     */
    Either<String, T> appendDirectChild(T child);

    /**
     * Removes the given child from the current tree's direct children.
     *
     * @param child the child
     *
     * @return an {@link Optional} containing the removed child
     */
    Optional<T> removeDirectChild(T child);

    /**
     * Retrieves child node with the given identity.
     *
     * <p>
     *
     * As opposed to {@link #appendDirectChild(Identifiable)} and {@link #removeDirectChild(Identifiable)},
     * this method will recursively search for the given identity.
     *
     * @param identity  the identity of the requested child
     *
     * @return the retrieved child
     */
    Optional<T> getChildWithIdentity(@NonNull I identity);

    /**
     * Retrieves the child matching the given predicate.
     *
     * <p>
     *
     * As opposed to {@link #appendDirectChild(Identifiable)} and {@link #removeDirectChild(Identifiable)},
     * this method will recursively search for the given match.
     *
     * @param predicate  the {@link Predicate} to match
     *
     * @return the matching child
     *
     * @see Predicate
     */
    Optional<T> getChildMatching(@NonNull Predicate<T> predicate);

    /**
     * Returns whether the current tree implementation has any child with the given identity.
     *
     * <p>
     *
     * As opposed to {@link #appendDirectChild(Identifiable)} and {@link #removeDirectChild(Identifiable)},
     * this method will recursively search for the given identity.
     *
     * @param identity  the identity of the requested node
     *
     * @return whether the current tree implementation has any child with the given identity.
     */
    boolean hasChildWithIdentity(@NonNull I identity);
}
