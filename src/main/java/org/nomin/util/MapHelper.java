package org.nomin.util;

import org.nomin.core.NominException;
import org.nomin.core.preprocessing.Preprocessing;
import java.util.*;
import static java.text.MessageFormat.format;

/**
* @author Dmitry Dobrynin
*         Date: 25.10.2010 Time: 21:54:38
*/
@SuppressWarnings({"unchecked"})
public class MapHelper extends ContainerHelper {
    public MapHelper(TypeInfo typeInfo) {
        // TODO: Preprocessing should be applied to the map key!
        super(typeInfo.getType(), typeInfo.getParameter(1));
    }

    public Map<Object, Object> createContainer(int size) throws Exception {
        if (containerClass == Map.class) return new HashMap<Object, Object>();
        else if (!containerClass.isInterface()) return (Map<Object, Object>) containerClass.newInstance();
        throw new NominException(format("Could not instantiate {0}!", containerClass.getName()));
    }

    public Object convert(Collection<Object> source, Preprocessing preprocessing) throws Exception {
        Map<Object, Object> target = createContainer(source.size());
        for (Object e : source) {
            Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) e;
            target.put(entry.getKey(), preprocessing != null ? preprocessing.preprocess(entry.getValue()) : entry.getValue());
        }
        return target;
    }

    public Object setElement(Object target, Object index, Object element, Preprocessing preprocessing) throws Exception {
        Map<Object, Object> targetMap = target != null ? (Map<Object, Object>) target :
                createContainer(1);
        targetMap.put(index, preprocessing != null ? preprocessing.preprocess(element) : element);
        return targetMap;
    }

    public Object getElement(Object source, Object index) { return ((Map<Object, Object>) source).get(index); }
}
