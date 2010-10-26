package org.nomin.util;

import java.lang.reflect.Method;

/**
 * Contains propertyAccessor methods.
 *
 * @author Dmitry Dobrynin
 *         Created: 18.04.2010 14:28:28
 */
public class JbPropertyAccessor implements PropertyAccessor {
    private String name;
    private Method setter, getter;
    private TypeInfo typeInfo;

    public JbPropertyAccessor(String name, Method getter, Method setter, TypeInfo typeInfo) {
        this.name = name;
        this.getter = getter;
        this.setter = setter;
        this.typeInfo = typeInfo;
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

    public Object newOwner() throws Exception {
        if (getter != null) return getter.getDeclaringClass().newInstance();
        return setter.getDeclaringClass().newInstance();
    }

    public Object get(Object instance) throws Exception {
        return getter.invoke(instance);
    }

    public void set(Object instance, Object value) throws Exception {
        setter.invoke(instance, value);
    }
}
