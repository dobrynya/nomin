package org.nomin.util;

import java.lang.reflect.*;
import org.nomin.core.*;
import static java.text.MessageFormat.format;

/**
 * Contains accessor methods.
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

    public Object get(Object instance) {
        try {
            return getter.invoke(instance);
        } catch (Exception e) {
            throw new NominException(format("Could not get property {0} on {1}!", name, instance), e);
        }
    }

    public void set(Object instance, Object value) {
        try {
            setter.invoke(instance, value);
        } catch (Exception e) {
            throw new NominException(format("Could not set property {0} on {1}!", name, instance), e);
        }
    }
}
