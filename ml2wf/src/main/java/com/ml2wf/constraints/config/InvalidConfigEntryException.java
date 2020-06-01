package com.ml2wf.constraints.config;

/**
 * Thrown to indicate that the given {@code Config} entry is invalid.
 *
 * <p>
 *
 * An invalid entry can be an empty entry or an entry with missing name. Both of
 * these cases are unrecoverable because of the impossibility to identify which
 * one of the required operators is invalid/missing.
 *
 * <p>
 *
 * <b>Note</b> that an incomplete entry which is recoverable will throw an
 * {@link IncompleteConfigEntryException}.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see Config
 * @see IncompleteConfigEntryException
 */
public class InvalidConfigEntryException extends Exception {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 7999280215768970821L;

	/**
	 * {@code InvalidConfigEntryException}'s default constructor.
	 *
	 * @param message message to print.
	 */
	public InvalidConfigEntryException(String message) {
		super(message);
	}
}
