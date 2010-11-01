package org.nomin.core.preprocessing;

/**
 * Applies the requested preprocessing if it exists.
 * @author Dmitry Dobrynin
 *         Date: 01.11.2010 Time: 23:24:10
 */
public class PreprocessingHelper {
    /**
     * Applies the requested preprocessing.
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
