package org.nomin.core.preprocessing;

/**
 * Provides an operation to preprocess an object being mapped.
 * @author Dmitry Dobrynin
 *         Created 28.04.2010 15:19:43
 */
public abstract class Preprocessing {
    /**
     * Preprocesses an object.
     * @param source a source object
     * @return processed object
     */
    abstract Object preprocess(Object source);

    /**
     * Applies the requested preprocessing if it exists.
     * @param value value to be preprocessed
     * @param preprocessings available preprocessings
     * @param idx index of the requested preprocessing
     * @return the original or preprocessed object
     */
    public static Object preprocess(Object value, Preprocessing[] preprocessings, int idx) {
        return preprocessings != null && preprocessings.length > idx && preprocessings[idx] != null ?
                preprocessings[idx].preprocess(value) : value;
    }
}
