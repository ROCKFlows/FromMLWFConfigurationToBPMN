package com.ml2wf.tasks.specs;

import java.util.Optional;

import com.ml2wf.tasks.base.Task;

/**
 * This interface provides methods for the <b>recovery and application of
 * {@code T}'s specifications</b>.
 *
 * @author Nicolas Lacroix
 *
 * @param <T> Type containing specifications
 *
 * @see Task
 *
 * @since 1.0.0
 */
public interface Spec<T> {

    /**
     * Returns whether the given {@code element} has the current specification or
     * not.
     *
     * @param element element to test
     *
     * @return whether the given {@code element} has the current specification or not
     */
    boolean hasSpec(T element);

    /**
     * Returns an {@code Optional} that contains the given {@code element}'s
     * specification value.
     *
     * @param element element to test
     *
     * @return the given {@code element}'s specification value
     *
     * @see Optional
     */
    Optional<String> getSpecValue(T element);

    /**
     * Retrieves and applies the current specification value of the given
     * {@code element}.
     *
     * @param element element to update the current specification value
     */
    void apply(T element);

    /**
     * Retrieves and applies all the specifications of the given
     * {@code element}.
     *
     * @param element element to update the current specification value
     */
    void applyAll(T element);
}
