package org.nomin.util;

import java.lang.reflect.Method;

/**
 * Contains property accessor methods and provides accessing a property.
 * @author Dmitry Dobrynin
 *         Created: 18.04.2010 14:28:28
 */
public class ReflectionPropertyAccessor implements PropertyAccessor {
    private String name;
    private Method setter, getter;
    private TypeInfo typeInfo;

    public ReflectionPropertyAccessor(String name, Method getter, Method setter, TypeInfo typeInfo) {
        this.name = name;
        this.getter = getter;
        this.setter = setter;
        this.typeInfo = typeInfo;
    }

    public String getName() { return name; }

    public TypeInfo getTypeInfo() { return typeInfo; }

    public Object get(Object instance) throws Exception { return getter.invoke(instance); }

    public void set(Object instance, Object value) throws Exception { setter.invoke(instance, value); }
}
