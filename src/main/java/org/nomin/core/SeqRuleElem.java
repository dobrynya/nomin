package org.nomin.core;

import org.nomin.util.*;

import static java.text.MessageFormat.format;

/**
 * Provides access to an indexed element of collections or arrays.
 * @author Dmitry Dobrynin
 *         Created: 27.04.2010 22:53:37
 */
public class SeqRuleElem extends RuleElem {
    final Object index;
    final InstanceCreator instanceCreator;

    public SeqRuleElem(Object index, TypeInfo typeInfo, ContainerHelper containerHelper, InstanceCreator instanceCreator) {
        super(typeInfo);
        this.index = index;
        this.containerHelper = containerHelper;
        this.instanceCreator = instanceCreator;
    }

    public Object get(Object instance) throws Exception {
        return instance == null ? null : next == null ?
                containerHelper.getElement(instance, index) : next.get(containerHelper.getElement(instance, index));
    }

    public Object set(Object instance, Object value) throws Exception {
        if (next == null) return containerHelper.setElement(instance, index, value, preprocessings);
        Object ov = containerHelper.getElement(instance, index),
                nv = next.set(ov == null ? instanceCreator.create(typeInfo.determineTypeDynamically(value)) : ov, value);
        return ov != nv ? containerHelper.setElement(instance, index, nv, preprocessings) : instance;
    }

    public String toString() { return format("[{0}]:{1}", index, typeInfo); }
}
