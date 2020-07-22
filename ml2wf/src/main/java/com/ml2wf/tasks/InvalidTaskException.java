package com.ml2wf.tasks;

/**
 * This exception is thrown when a fatal error occurs during a {@code Task}
 * creation.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 * @see Task
 *
 */
public class InvalidTaskException extends Exception {

	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -1126515282699788030L;

	/**
	 * {@code InvalidTaskException}'s default constructor.
	 *
	 * @param arg0 exception explanation
	 */
	public InvalidTaskException(String arg0) {
		super(arg0);
	}

}
