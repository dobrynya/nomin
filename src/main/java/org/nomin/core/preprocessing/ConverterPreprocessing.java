package org.nomin.core.preprocessing;

import org.nomin.util.ScalarConverter;

/**
 * Converts a value using provided {@link org.nomin.util.ScalarConverter}.
 * @author Dmitry Dobrynin
 *         Created 28.04.2010 15:41:32
 */
public class ConverterPreprocessing extends Preprocessing {
    private ScalarConverter<Object, Object> scalarConverter;

    public ConverterPreprocessing(ScalarConverter<Object, Object> scalarConverter) {
        this.scalarConverter = scalarConverter;
    }

    public Object preprocess(Object source) {
        return source != null ? scalarConverter.convert(source) : null;
    }
}
