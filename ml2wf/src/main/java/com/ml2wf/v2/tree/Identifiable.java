package com.ml2wf.v2.tree;

import lombok.NonNull;

/**
 * This {@link FunctionalInterface} defines the main method for identifying
 * an object.
 *
 * <p>
 *
 * Useful for internal memories indexing objects using their identity.
 *
 * @param <T> the identity type
 *
 * @since 1.1.0
 */
@FunctionalInterface
public interface Identifiable<T> {

    /**
     * Returns the current implementation's identity.
     *
     * @return the current implementation's identity
     */
    @NonNull T getIdentity();
}
