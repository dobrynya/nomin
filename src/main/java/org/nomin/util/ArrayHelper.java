package org.nomin.util;

import org.nomin.core.preprocessing.Preprocessing;

import java.util.Arrays;
import java.util.Collection;

/**
* @author Dmitry Dobrynin
*         Date: 25.10.2010 Time: 21:54:18
*/
public class ArrayHelper extends ContainerHelper {
    public ArrayHelper(TypeInfo typeInfo) {
        super(typeInfo.getParameter(0).getType(), typeInfo.getParameter(0));
    }

    public Object[] createContainer(int size) throws Exception {
        return (Object[]) java.lang.reflect.Array.newInstance(containerClass, size);
    }

    public Object convert(Collection<Object> source, Preprocessing preprocessing) throws Exception {
        Object[] target = createContainer(source.size());
        int index = 0;
        for (Object object : source) target[index++] = preprocessing != null ? preprocessing.preprocess(object) : object;
        return target;
    }

    public Object setElement(Object target, Object index, Object element, Preprocessing preprocessing) throws Exception {
        Integer i = (Integer) index;
        Object[] targetArray = (Object[]) target;
        if (targetArray != null && i >= targetArray.length) targetArray = Arrays.copyOf(targetArray, i + 1);
        else targetArray = createContainer(i + 1);
        targetArray[i] = preprocessing != null ? preprocessing.preprocess(element) : element;
        return targetArray;
    }

    public Object getElement(Object source, Object index) {
        Object[] array = (Object[]) source;
        return array.length > (Integer) index ? array[(Integer) index] : null;
    }
}
