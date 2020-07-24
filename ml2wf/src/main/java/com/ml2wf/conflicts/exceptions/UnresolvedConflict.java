package com.ml2wf.conflicts.exceptions;

import com.ml2wf.conflicts.ConflictSolver;

/**
 * This exception is thrown when a conflict still not resolved after been
 * processed by a {@link ConflictSolver} implementation.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 * @see ConflictSolver
 *
 */
public class UnresolvedConflict extends Exception {

	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = 5994738837557737546L;

	/**
	 * {@code UnsolvedConflict}'s default constructor.
	 *
	 * @param arg0 exception explanation
	 */
	public UnresolvedConflict(String arg0) {
		super(arg0);
	}
}
