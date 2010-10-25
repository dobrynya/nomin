package org.nomin.core.preprocessing;

import groovy.lang.Closure;

/**
 * Applies conversion to value.
 * @author Dmitry Dobrynin
 *         Created: 29.04.2010 1:05:17
 */
public class ConversionPreprocessing implements Preprocessing {
    private Closure conversion;

    public ConversionPreprocessing(Closure conversion) {
        this.conversion = conversion;
    }

    public Object preprocess(Object source) {
        return conversion.call(source);
    }
}
