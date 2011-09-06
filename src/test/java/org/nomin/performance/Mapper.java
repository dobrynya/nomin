package org.nomin.performance;

/**
 * Unifies Nomin and Dozer interfaces.
 * @author Dmitry Dobrynin
 *         Created 04.09.11 11:43
 */
public interface Mapper {
    Object map(Object source, Class<?> targetClass);
}
