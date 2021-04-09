package com.ml2wf.merge.exceptions;

import com.ml2wf.merge.base.BaseMerger;

/**
 * This exception is thrown when a fatal error occurs during a merge operation.
 *
 * @author Nicolas Lacroix
 *
 * @see BaseMerger
 *
 * @since 1.0.0
 */
public class MergeException extends Exception {

    /**
     * {@code MergeException}'s constructor with a message.
     *
     * @param message	the exception's message
     */
    public MergeException(String message) {
        super(message);
    }
}
