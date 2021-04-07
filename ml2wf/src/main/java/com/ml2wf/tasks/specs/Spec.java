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
 * @version 1.0
 *
 * @see Task
 */
public interface Spec<T> {

	/**
	 * Returns whether the given {@code element} has the current specification or
	 * not.
	 *
	 * @param element element to test
	 * @return whether the given {@code element} has the current specification or
	 *         not
	 *
	 * @since 1.0
	 */
	public boolean hasSpec(T element);

	/**
	 * Returns an {@code Optional} that contains the given {@code element}'s
	 * specification value.
	 *
	 * @param element element to test
	 * @return the given {@code element}'s specification value
	 *
	 * @since 1.0
	 *
	 * @see Optional
	 */
	public Optional<String> getSpecValue(T element);

	/**
	 * Retrieves and applies the current specification value of the given
	 * {@code element}.
	 *
	 * @param element element to update the current specification value
	 *
	 * @since 1.0
	 */
	public void apply(T element);

	/**
	 * Retrieves and applies all the specifications of the given
	 * {@code element}.
	 *
	 * @param element element to update the current specification value
	 *
	 * @since 1.0
	 */
	public void applyAll(T element);
}
