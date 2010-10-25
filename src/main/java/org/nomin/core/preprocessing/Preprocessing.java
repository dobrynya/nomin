package org.nomin.core.preprocessing;

/**
 * Provides an operation to preprocess an object being mapped.
 * @author Dmitry Dobrynin
 *         Created 28.04.2010 15:19:43
 */
public interface Preprocessing {

    /**
     * Preprocesses an object.
     * @param source a source object
     * @return processed object
     */
    Object preprocess(Object source);
}
