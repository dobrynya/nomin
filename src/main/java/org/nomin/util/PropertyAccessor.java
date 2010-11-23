package org.nomin.util;

/**
 * Accesses a property.
 * @author Dmitry Dobrynin
 *         Created: 18.04.2010 14:25:07
 */
public interface PropertyAccessor {
    String getName();

    TypeInfo getTypeInfo();

    Object get(Object instance) throws Exception;

    void set(Object instance, Object value) throws Exception;
}
