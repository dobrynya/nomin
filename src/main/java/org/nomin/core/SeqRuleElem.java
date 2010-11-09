package org.nomin.core;

import org.nomin.util.ContainerHelper;
import org.nomin.util.TypeInfo;

import static java.text.MessageFormat.format;

/**
 * Provides access to an indexed element of collections or arrays.
 * @author Dmitry Dobrynin
 *         Created: 27.04.2010 22:53:37
 */
public class SeqRuleElem extends RuleElem {
    final Object index;

    public SeqRuleElem(Object index, TypeInfo typeInfo, ContainerHelper containerHelper) {
        super(typeInfo);
        this.index = index;
        this.containerHelper = containerHelper;
    }

    public Object get(Object instance) throws Exception {
        return instance == null ? null : next == null ?
                containerHelper.getElement(instance, index) : next.get(containerHelper.getElement(instance, index));
    }

    public Object set(Object instance, Object value) throws Exception {
        return containerHelper.setElement(instance, index,
                next != null ? next.set(containerHelper.getElement(instance, index), value) : value, preprocessings);
    }

    public String toString() { return format("[{0}]:{1}", index, typeInfo); }
}
