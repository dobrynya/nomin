package org.nomin.util;

import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;

/**
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

    public String getName() {
        return name;
    }

    public TypeInfo getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(TypeInfo typeInfo) {

    }

    public Object get(Object instance) throws Exception {
        try {
            return getter.invoke(instance, new Object[0]);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("", e);
        }
    }

    public void set(Object instance, Object value) throws Exception {
        try {
            setter.invoke(instance, new Object[] { value });
        } catch (InvocationTargetException e) {
            throw new RuntimeException("",e );
        }
    }
}
