package org.nomin.util;

import org.nomin.core.NominException;
import org.nomin.core.preprocessing.Preprocessing;
import java.util.*;
import static java.text.MessageFormat.format;
import static org.nomin.core.preprocessing.PreprocessingHelper.preprocess;

/**
 * Assists to manipulate with maps.
* @author Dmitry Dobrynin
*         Date: 25.10.2010 Time: 21:54:38
*/
@SuppressWarnings({"unchecked"})
public class MapHelper extends ContainerHelper {
    public MapHelper(TypeInfo typeInfo) { super(typeInfo.getType(), typeInfo.getParameter(1)); }

    public Map<Object, Object> createContainer(int size) throws Exception {
        if (containerClass == Map.class) return new HashMap<Object, Object>();
        else if (!containerClass.isInterface()) return (Map<Object, Object>) containerClass.newInstance();
        throw new NominException(format("Could not instantiate {0}!", containerClass.getName()));
    }

    public Object convert(Collection<Object> source, Preprocessing[] preprocessings) throws Exception {
        Map<Object, Object> target = createContainer(source.size());
        for (Object e : source) {
            Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) e;
            target.put(preprocess(entry.getKey(), preprocessings, 0), preprocess(entry.getValue(), preprocessings, 1));
        }
        return target;
    }

    public Object setElement(Object target, Object index, Object element, Preprocessing[] preprocessings) throws Exception {
        Map<Object, Object> targetMap = target != null ? (Map<Object, Object>) target : createContainer(1);
        targetMap.put(index, preprocess(element, preprocessings, 1));
        return targetMap;
    }

    public Object getElement(Object source, Object index) { return ((Map<Object, Object>) source).get(index); }
}
