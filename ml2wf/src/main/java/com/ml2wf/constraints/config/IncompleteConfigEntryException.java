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
 * @version 1.0
 *
 * @see Config
 * @see InvalidConfigEntryException
 */
public class IncompleteConfigEntryException extends Exception {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 392862737117228562L;

	/**
	 * {@code IncompleteConfigEntryException}'s default constructor.
	 *
	 * @param message message to print.
	 */
	public IncompleteConfigEntryException(String message) {
		super(message);
	}
}
