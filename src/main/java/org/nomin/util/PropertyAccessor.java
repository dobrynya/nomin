package org.nomin.util;

import org.nomin.core.TypeInfo;

/**
 * Accesses a property.
 * @author Dmitry Dobrynin
 *         Created: 18.04.2010 14:25:07
 */
public interface PropertyAccessor {
    String getName();

    TypeInfo getTypeInfo();

    void setTypeInfo(TypeInfo typeInfo);

    Object get(Object instance);

    void set(Object instance, Object value);
}
