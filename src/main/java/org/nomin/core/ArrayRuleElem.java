package org.nomin.core;

import org.nomin.util.PropertyAccessor;
import org.nomin.core.preprocessing.Preprocessing;
import java.lang.reflect.Array;

/**
 * Provides access to arrays.
 * @author Dmitry Dobrynin
 *         Created: 15.05.2010 16:34:51
 */
public class ArrayRuleElem extends CollectionRuleElem {
    public ArrayRuleElem(TypeInfo typeInfo, PropertyAccessor property) {
        super(typeInfo, property);
    }

    public Object get(Object instance) {
        return next instanceof ArraySeqRuleElem ? next.get(instance) : super.get(instance);
    }

    public void set(Object instance, Object value, Preprocessing preprocessing) {
        if (next != null) {
            if (next instanceof ArraySeqRuleElem) {
                next.set(instance, value, preprocessing);
            } else {
                Object current = retrieve(instance);
                if (current == null) {
                    current = initialize(instance, value);
                }
                next.set(current, value, preprocessing);
            }
        } else {
            store(instance, value, preprocessing);
        }
    }

    Object createContainer(int size) {
        return Array.newInstance(property.getTypeInfo().determineType(), size);
    }

    protected void store(Object instance, Object value, Preprocessing preprocessing) {
        Object[] result = (Object[]) initialize(instance, value);
        int i = 0;
        // Optimizing performance: checking preprocessing only once
        if (preprocessing != null) for (Object o : iterable(value)) result[i++] = preprocessing.preprocess(o, null);
        else for (Object o : iterable(value)) result[i++] = o;
    }
}
