package org.nomin.util;

import org.nomin.core.*;
import java.lang.reflect.Field;

/**
 * Documentation.
 *
 * @author Dmitry Dobrynin
 *         Created: 21.05.2010 0:52:28
 */
public class FieldPropertyAccessor implements PropertyAccessor {
    private String name;
    private TypeInfo typeInfo;
    private Field property;

    public FieldPropertyAccessor(String name, TypeInfo typeInfo, Field property) {
        this.name = name;
        this.typeInfo = typeInfo;
        this.property = property;
        if (!property.isAccessible()) property.setAccessible(true);
    }

    public String getName() {
        return name;
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(TypeInfo typeInfo) {
        this.typeInfo = typeInfo;
    }

    public Object get(Object instance) {
        try {
            return property.get(instance);
        } catch (IllegalAccessException e) {
            throw new NominException("Could not get field value!", e);
        }
    }

    public void set(Object instance, Object value) {
        try {
            property.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new NominException("Could not set field value!", e);
        }
    }
}
