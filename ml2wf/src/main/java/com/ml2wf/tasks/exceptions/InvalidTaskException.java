package com.ml2wf.tasks.exceptions;

/**
 * This exception is thrown when a fatal error occurs during a {@code Task}
 * creation.
 *
 * @author Nicolas Lacroix
 *
 * @since 1.0.0
 */
public class InvalidTaskException extends RuntimeException {

    /**
     * {@code InvalidTaskException}'s constructor with a message.
     *
     * @param message   the exception's message
     */
    public InvalidTaskException(String message) {
        super(message);
    }
}
