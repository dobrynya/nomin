package org.nomin.core;

import org.nomin.core.preprocessing.Preprocessing;
import org.nomin.util.TypeInfo;

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
            array[index] = result;
            return result;
        } catch (Exception e) {
            throw new NominException("Could not instantiate a " + typeInfo.determineTypeDynamically(value).getSimpleName() + "!", e);
        }
    }

    protected Object retrieve(Object instance) {
        Object[] array = (Object[]) prev.retrieve(instance);
        return array != null && array.length > index ? array[index] : null;
    }

    protected void store(Object instance, Object value, Preprocessing preprocessing) {
        // instance is an object containing an array property, it is done so to be able to substitute an array
        Object[] merged = mergeArrays((Object[]) prev.retrieve(instance));
        prev.property.set(instance, merged);
        merged[index] = preprocessing != null ? preprocessing.preprocess(value, null) : value;
    }

    protected Object[] mergeArrays(Object[] array) {
        if (array == null || array.length == 0) return (Object[]) prev.createContainer(index + 1);
        else if (array.length > index) return array;
        else {
            Object[] result = (Object[]) prev.createContainer(index + 1);
            System.arraycopy(array, 0, result, 0, array.length);
            return result;
        }
    }
}
