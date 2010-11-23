package org.nomin.util;

import java.util.*;
import org.nomin.core.preprocessing.Preprocessing;
import static org.nomin.core.preprocessing.Preprocessing.preprocess;

/**
 * Assists to manipulate with arrays.
 * @author Dmitry Dobrynin
 *         Date: 25.10.2010 Time: 21:54:18
*/
public class ArrayHelper extends ContainerHelper {
    public ArrayHelper(TypeInfo typeInfo) {
        super(typeInfo.getParameter(0).type, typeInfo.getParameter(0));
    }

    public Object[] createContainer(int size) throws Exception {
        return (Object[]) java.lang.reflect.Array.newInstance(containerClass, size);
    }

    public Object convert(Collection<Object> source, Preprocessing[] preprocessings) throws Exception {
        Object[] target = createContainer(source.size());
        int index = 0;
        for (Object object : source) target[index++] = preprocess(object, preprocessings, 0);
        return target;
    }

    public Object setElement(Object target, Object index, Object element, Preprocessing[] preprocessing) throws Exception {
        Integer i = (Integer) index;
        Object[] targetArray = (Object[]) target;
        if (targetArray != null && i >= targetArray.length) targetArray = Arrays.copyOf(targetArray, i + 1);
        else targetArray = createContainer(i + 1);
        targetArray[i] = preprocessing != null ? preprocess(element, preprocessing, 0) : element;
        return targetArray;
    }

    public Object getElement(Object source, Object index) {
        Object[] array = (Object[]) source;
        return array.length > (Integer) index ? array[(Integer) index] : null;
    }
}
