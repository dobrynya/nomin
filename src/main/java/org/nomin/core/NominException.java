package org.nomin.core;

/**
 * Thrown when an error occurs.
 * @author Dmitry Dobrynin
 *         Created: 09.05.2010 21:42:46
 */
public class NominException extends RuntimeException {
    protected boolean shouldWrap = false;

    public NominException(String message) { super(message); }

    public NominException(boolean shouldWrap, String message) {
        super(message);
        this.shouldWrap = shouldWrap;
    }

    public NominException(String message, Throwable cause) { super(message, cause); }

    public NominException(boolean shouldWrap, String message, Throwable cause) {
        super(message, cause);
        this.shouldWrap = shouldWrap;
    }

    public boolean shouldWrap() { return shouldWrap; }
}
