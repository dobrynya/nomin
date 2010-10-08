package org.nomin.core;

import java.lang.reflect.*;
import java.text.MessageFormat;
import java.util.*;
import groovy.lang.Closure;

import static java.text.MessageFormat.*;
import static java.util.Arrays.asList;

/**
 * Holds all information about a type.
 * @author Dmitry Dobrynin
 *         Created: 08.05.2010 16:38:47
 */
public class TypeInfo {
    Class<?> type;
    Closure dynamicType;
    List<TypeInfo> parameters;
    boolean collection;
    boolean map;
    boolean array;

    public TypeInfo(Class<?> type) {
        this.type = type;
        collection = Collection.class.isAssignableFrom(type);
        map = Map.class.isAssignableFrom(type);
        array = type.isArray() || type == Array.class;
    }

    public TypeInfo(Class<?> type, List<TypeInfo> parameters) {
        this(type);
        this.parameters = parameters;
    }

    public TypeInfo(Closure dynamicType) { this.dynamicType = dynamicType; }

    public boolean isCollection() { return collection; }

    public boolean isMap() { return map; }

    public boolean isArray() { return array; }

    public boolean isContainer() { return collection || array || map; }

    public boolean isUndefined() { return type == Undefined.class; }

    public boolean isDynamic() {
        if (parameters != null) for (TypeInfo ti : parameters) {
            if (ti.isDynamic()) return true;
        }
        return dynamicType != null;
    }

    public Closure getDynamicType() {
        return parameters != null && !parameters.isEmpty() ? parameters.get(0).dynamicType : dynamicType;
    }

    public void merge(TypeInfo typeInfo) {
        if (parameters == null && typeInfo.parameters != null) parameters = typeInfo.parameters;
        else if (parameters != null && typeInfo.parameters != null) {
            for (int i = 0; i < Math.max(parameters.size(), typeInfo.parameters.size()); i++)
                if (i < parameters.size()) {
                    if (i < typeInfo.parameters.size()) parameters.get(i).merge(typeInfo.parameters.get(i));
                } else {
                    parameters.add(typeInfo.parameters.get(i));
                }
        }
        if (typeInfo.type != null) type = typeInfo.type;
        else if (typeInfo.dynamicType != null) dynamicType = typeInfo.dynamicType;
    }

    public Class<?> determineType() {
        if (isContainer()) {
            return parameters != null && !parameters.isEmpty() ? parameters.get(0).determineType() : Object.class;
        }
        return type;
    }

    public Class<?> determineTypeDynamically(Object instance) {
        if (isContainer()) {
            return parameters != null && !parameters.isEmpty() ?
                    parameters.get(0).determineTypeDynamically(instance) : Object.class;
        } else if (dynamicType != null) {
            return (Class<?>) dynamicType.call(instance);
        }
        return type;
    }

    public static TypeInfo typeInfo(Object type) {
        if (Class.class.isInstance(type)) {
            Class<?> clazz = (Class<?>) type;
            if (clazz.isArray()) {
                return new TypeInfo(clazz, asList(new TypeInfo(clazz.getComponentType())));
            } else {
                return new TypeInfo((Class<?>) type);
            }
        } else if (ParameterizedType.class.isInstance(type)) {
            ParameterizedType pt = (ParameterizedType) type;
            TypeInfo result = new TypeInfo((Class<?>) pt.getRawType());
            result.parameters = new ArrayList<TypeInfo>(pt.getActualTypeArguments().length);
            for (Type t : pt.getActualTypeArguments()) {
                result.parameters.add(typeInfo(t));
            }
            return result;
        } else if (type instanceof Closure) {
            return new TypeInfo((Closure) type);
        }
        throw new IllegalArgumentException("Could not recognize type " + type + "!");
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
