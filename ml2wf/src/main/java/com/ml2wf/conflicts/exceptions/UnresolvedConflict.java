package com.ml2wf.conflicts.exceptions;

import com.ml2wf.conflicts.ConflictSolver;

/**
 * This exception is thrown when a conflict still not resolved after been
 * processed by a {@link ConflictSolver} implementation.
 *
 * @author Nicolas Lacroix
 *
 * @see ConflictSolver
 *
 * @since 1.0.0
 */
public class UnresolvedConflict extends Exception {

    /**
     * {@code UnsolvedConflict}'s constructor with a message.
     *
     * @param message   the exception's message
     */
    public UnresolvedConflict(String message) {
        super(message);
    }

    /**
     * {@code UnsolvedConflict}'s constructor with a format message.
     *
     * @param format    the exception's format base
     * @param args      the exception's format arguments
     *
     * @see String#format(String, Object...)
     */
    public UnresolvedConflict(String format, Object... args) {
        super(String.format(format, args));
    }
}
