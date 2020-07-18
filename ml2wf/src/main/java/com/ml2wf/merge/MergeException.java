package com.ml2wf.merge;

import com.ml2wf.merge.base.BaseMergerImpl;

/**
 * This exception is thrown when a fatal error occurs during a merge operation.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 * @see BaseMergerImpl
 *
 */
public class MergeException extends Exception {

	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -6338472968929435621L;

	public MergeException(String arg0) {
		super(arg0);
	}

}