package org.nomin.util;

import org.nomin.core.preprocessing.Preprocessing;

import java.util.Collection;

/**
 * Assists to manipulate with containers.
 * @author Dmitry Dobrynin
 *         Date: 25.10.2010 Time: 21:51:31
 */
public abstract class ContainerHelper {
    protected TypeInfo elementType;
    protected Class<?> containerClass;

    protected ContainerHelper(Class<?> containerClass, TypeInfo elementType) {
        this.containerClass = containerClass;
        this.elementType = elementType;
    }

    public abstract Object createContainer(int size) throws Exception;

    public abstract Object convert(Collection<Object> source, Preprocessing[] preprocessings) throws Exception;

    public abstract Object setElement(Object target, Object index, Object element, Preprocessing[] preprocessing) throws Exception;

    public abstract Object getElement(Object source, Object index);

    public TypeInfo getElementType() { return elementType; }

    /**
     * Creates an appropriate ContainerHelper instance.
     * @param typeInfo specifies the type
     * @return an appropriate ContainerHelper
     */
    public static ContainerHelper create(TypeInfo typeInfo) {
        return typeInfo.isArray() ? new ArrayHelper(typeInfo) :
                typeInfo.isCollection() ? new CollectionHelper(typeInfo) :
                        typeInfo.isMap() ? new MapHelper(typeInfo) : null;
    }
}
