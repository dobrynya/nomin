package org.nomin.core;

/**
 * Thrown when an error occurs.
 * @author Dmitry Dobrynin
 *         Created: 09.05.2010 21:42:46
 */
public class NominException extends RuntimeException {
    public NominException() {}

    public NominException(String message) {
        super(message);
    }

    public NominException(String message, Throwable cause) {
        super(message, cause);
    }

    public NominException(Throwable cause) {
        super(cause);
    }
}
