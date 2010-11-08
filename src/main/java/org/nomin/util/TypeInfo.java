package org.nomin.util;

import java.util.*;
import groovy.lang.Closure;
import org.nomin.core.*;
import static java.text.MessageFormat.*;
import static java.util.Collections.*;

/**
 * Holds all information about a type.
 * @author Dmitry Dobrynin
 *         Created: 08.05.2010 16:38:47
 */
public class TypeInfo {
    public final Class<?> type;
    public final Closure dynamicType;
    public final List<TypeInfo> parameters;
    public final boolean collection;
    public final boolean map;
    public final boolean array;

    public TypeInfo(Class<?> type, Closure dynamicType, List<TypeInfo> parameters) {
        this.type = type;
        this.dynamicType = dynamicType;
        this.parameters = parameters;
        if (type != null) {
            collection = Collection.class.isAssignableFrom(type);
            map = Map.class.isAssignableFrom(type);
            array = type.isArray() || type == org.nomin.core.Array.class;
        } else { collection = false; map = false; array = false; }
    }

    public TypeInfo(Class<?> type) { this(type, null, Collections.<TypeInfo>emptyList()); }

    public TypeInfo(Class<?> type, List<TypeInfo> parameters) { this(type, null, parameters); }

    public TypeInfo(Closure dynamicType) { this(null, dynamicType, Collections.<TypeInfo>emptyList()); }

    public TypeInfo getParameter(int i) {
        return parameters != null && i < parameters.size() ? parameters.get(i) : TypeInfoFactory.typeInfo(Object.class);
    }

    public boolean isContainer() { return collection || array || map; }

    public boolean isUndefined() { return type == Undefined.class; }

    public boolean isDynamic() {
        for (TypeInfo ti : parameters) if (ti.isDynamic()) return true;
        return dynamicType != null;
    }

    public TypeInfo merge(TypeInfo typeInfo) { return typeInfo != null ? TypeInfoFactory.merge(this, typeInfo) : this; }

    public Class<?> determineTypeDynamically(Object instance) {
        if (array || collection)
            return parameters.size() == 1 ? parameters.get(0).determineTypeDynamically(instance) : Object.class;
        else if (map)
            return parameters.size() == 2 ? parameters.get(0).determineTypeDynamically(instance) : Object.class;
        else if (dynamicType != null)
            return (Class<?>) dynamicType.call(instance);
        return type;
    }

    public String toString() {
        if (map) return format("{0}[{1}, {2}]", type.getSimpleName(),
                parameters != null && parameters.size() > 0 ? parameters.get(0) : "Object",
                parameters != null && parameters.size() > 1 ? parameters.get(1) : "Object");
        if (array || collection) {
            return format("{0}[{1}]", array ? "Array" : type.getSimpleName(),
                    parameters != null && parameters.size() > 0 ? parameters.get(0) : "Object");
        }
        return dynamicType != null ? "{ expression }" : type.getSimpleName();
    }
}
