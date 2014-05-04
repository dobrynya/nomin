package org.nomin.util;

/**
 * Provides functionality to convert scalar types.
 * @param <S> specifies a source type
 * @param <T> specifies a target type
 * @author Dmitry Dobrynin
 *         Created at 26.04.2014 21:21.
 */
public interface ScalarConverter<S, T> {
    /**
     * Converts the specified value to desired type.
     * @param source specifies non-null source scalar value
     * @return value of desired type
     */
    T convert(S source);
}
