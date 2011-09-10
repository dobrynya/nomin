package org.nomin.performance;

/**
 * Unifies Nomin and Dozer interfaces.
 * @author Dmitry Dobrynin
 *         Created 04.09.11 11:43
 */
public interface MyMapper {
    Object map(Object source, Class<?> targetClass);
}
