package org.nomin.util;

import net.sf.cglib.reflect.FastMethod;

/**
 * Provides accessing a property using Cglib fast methods.
 * @author Dmitry Dobrynin
 *         Date: 21.10.2010 Time: 23:05:16
 */
public class FastPropertyAccessor implements PropertyAccessor {
    private String name;
    private FastMethod getter, setter;
    private TypeInfo typeInfo;

    public FastPropertyAccessor(String name, FastMethod getter, FastMethod setter, TypeInfo typeInfo) {
        this.name = name;
        this.getter = getter;
        this.setter = setter;
        this.typeInfo = typeInfo;
    }

    public String getName() { return name; }

    public TypeInfo getTypeInfo() { return typeInfo; }

    public Object get(Object instance) throws Exception { return getter.invoke(instance, empty); }

    public void set(Object instance, Object value) throws Exception {
        setter.invoke(instance, new Object[] { value });
    }

    private static Object[] empty = new Object[0];
}
