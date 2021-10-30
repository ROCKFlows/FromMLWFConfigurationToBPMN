package com.ml2wf.constraints.config;

/**
 * Thrown to indicate that the given {@code Config} entry is incomplete.
 *
 * <p>
 *
 * An incomplete entry can be an entry with missing values or with bad arity or
 * symbol format.
 *
 * <p>
 *
 * <b>Note</b> that an invalid name is unrecoverable and therefore will throw an
 * {@link InvalidConfigEntryException}.
 *
 * @author Nicolas Lacroix
 *
 * @see Config
 * @see InvalidConfigEntryException
 *
 * @since 1.0.0
 */
public class IncompleteConfigEntryException extends Exception {

    /**
     * {@code IncompleteConfigEntryException}'s default constructor.
     *
     * @param message message to print.
     */
    public IncompleteConfigEntryException(String message) {
        super(message);
    }
}
