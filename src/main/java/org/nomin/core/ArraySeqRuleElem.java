package org.nomin.core;

import org.nomin.core.preprocessing.Preprocessing;
import org.nomin.util.TypeInfo;
import static java.text.MessageFormat.format;

/**
 * Provides indexed access to arrays.
 * @author Dmitry Dobrynin
 *         Created: 16.05.2010 13:17:07
 */
public class ArraySeqRuleElem extends SeqRuleElem {
    final ArrayRuleElem prev;

    protected ArraySeqRuleElem(TypeInfo typeInfo, Integer index, ArrayRuleElem prev) {
        super(typeInfo, index);
        this.prev = prev;
    }

    protected Object initialize(Object instance, Object value) {
        try {
            Object result = typeInfo.determineTypeDynamically(value).newInstance();
            Object[] array = mergeArrays((Object[]) prev.retrieve(instance));
            prev.property.set(instance, array);
            array[index > 0 ? index : array.length - 1] = result;
            return result;
        } catch (Exception e) {
            throw new NominException(format("Could not instantiate a {0}!",
                    typeInfo.determineTypeDynamically(value).getSimpleName()), e);
        }
    }

    protected Object retrieve(Object instance) {
        Object[] array = (Object[]) prev.retrieve(instance);
        return index > 0 && array != null && array.length > index ? array[index] : null;
    }

    protected void store(Object instance, Object value, Preprocessing preprocessing) {
        // instance is an object containing an array property, it is done so to be able to substitute an array
        Object[] merged = mergeArrays((Object[]) prev.retrieve(instance));
        prev.property.set(instance, merged);
        merged[index > 0 ? index : merged.length - 1] = preprocessing != null ? preprocessing.preprocess(value, null) : value;
    }

    protected Object[] mergeArrays(Object[] current) {
        if (current == null || current.length == 0) return (Object[]) prev.createContainer(index > 0 ? index + 1 : 1);
        if ((index > 0 && current.length > index) || current[current.length - 1] == null) return current;
        Object[] result = (Object[]) prev.createContainer(index > 0 ? index + 1 : current.length + 1);
        System.arraycopy(current, 0, result, 0, current.length);
        return result;
    }
}
