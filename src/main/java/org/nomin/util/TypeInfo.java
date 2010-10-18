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
    Class<?> type;
    Closure dynamicType;
    List<TypeInfo> parameters = emptyList();
    boolean collection;
    boolean map;
    boolean array;

    public TypeInfo(Class<?> type) {
        this.type = type;
        collection = Collection.class.isAssignableFrom(type);
        map = Map.class.isAssignableFrom(type);
        array = type.isArray() || type == org.nomin.core.Array.class;
    }

    public TypeInfo(Class<?> type, List<TypeInfo> parameters) {
        this(type);
        this.parameters = parameters;
    }

    public TypeInfo(Closure dynamicType) { this.dynamicType = dynamicType; }

    public Class<?> getType() { return type; }

    public List<TypeInfo> getParameters() { return parameters; }

    public TypeInfo getParameter(int i) { 
        return parameters != null && i < parameters.size() ? parameters.get(i) : TypeInfoFactory.typeInfo(Object.class);
    }

    public boolean isCollection() { return collection; }

    public boolean isMap() { return map; }

    public boolean isArray() { return array; }

    public boolean isContainer() { return collection || array || map; }

    public boolean isUndefined() { return type == Undefined.class; }

    public boolean isDynamic() {
        for (TypeInfo ti : parameters) if (ti.isDynamic()) return true;
        return dynamicType != null;
    }

    public Closure getDynamicType() {
        return parameters.size() > 0 ? parameters.get(0).dynamicType : dynamicType;
    }

    public void merge(TypeInfo typeInfo) {
        if (parameters.isEmpty() && !typeInfo.parameters.isEmpty()) parameters = typeInfo.parameters;
        else if (!parameters.isEmpty() && !typeInfo.parameters.isEmpty()) {
            for (int i = 0; i < Math.max(parameters.size(), typeInfo.parameters.size()); i++)
                if (i < parameters.size()) {
                    if (i < typeInfo.parameters.size()) parameters.get(i).merge(typeInfo.parameters.get(i));
                } else
                    parameters.add(typeInfo.parameters.get(i));
        }
        if (typeInfo.type != null) type = typeInfo.type;
        else if (typeInfo.dynamicType != null) dynamicType = typeInfo.dynamicType;
    }

    public Class<?> determineType() {
        if (array || collection) return parameters.size() == 1 ? parameters.get(0).determineType() : Object.class;
        if (map) return parameters.size() == 2 ? parameters.get(1).determineType() : Object.class;
        return type;
    }

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
