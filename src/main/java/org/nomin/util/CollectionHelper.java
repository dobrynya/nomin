package org.nomin.util;

import org.nomin.core.NominException;
import org.nomin.core.preprocessing.Preprocessing;
import java.util.*;
import java.util.concurrent.*;
import static org.nomin.core.preprocessing.Preprocessing.preprocess;
import static java.text.MessageFormat.format;

/**
 * Assists to manipulate with collections.
* @author Dmitry Dobrynin
*         Date: 25.10.2010 Time: 21:53:39
*/
@SuppressWarnings({"unchecked"})
public class CollectionHelper extends ContainerHelper {
    public CollectionHelper(TypeInfo typeInfo) {
        super(typeInfo.type, typeInfo.getParameter(0));
    }

    public Collection<Object> createContainer(int size) throws Exception {
        if (containerClass == Collection.class || containerClass == List.class) return new ArrayList<Object>(size);
        else if (containerClass == Set.class) return new HashSet<Object>(size);
        else if (containerClass == SortedSet.class || containerClass == NavigableSet.class) return new TreeSet<Object>();
        else if (containerClass == Queue.class || containerClass == Deque.class) return new LinkedList<Object>();
        else if (containerClass == BlockingQueue.class) return new LinkedBlockingQueue<Object>(size > 0 ? size : 1);
        else if (containerClass == BlockingDeque.class) return new LinkedBlockingDeque<Object>(size > 0 ? size : 1);
        else if (!containerClass.isInterface() && Collection.class.isAssignableFrom(containerClass))
            return (Collection<Object>) containerClass.newInstance();
        throw new NominException(true, format("Could not instantiate {0}!", containerClass.getName()));
    }

    public Collection<Object> convert(Collection<?> source, Preprocessing[] preprocessings) throws Exception {
        Collection<Object> target = createContainer(source.size());
        for (Object object : source) target.add(preprocess(object, preprocessings, 0));
        return target;
    }

    public Object setElement(Object target, Object index, Object element, Preprocessing[] preprocessings) throws Exception {
        int i = (Integer) index;
        if (target == null) target = createContainer(i < 0 ? 1 : i + 1);
        if (i < 0) {
            ((Collection) target).add(preprocess(element, preprocessings, 0));
            return target;
        }
        if (target instanceof List) return setListElement((List<Object>) target, i, element, preprocessings);
        ((Collection<Object>) target).clear();
        ((Collection<Object>) target).addAll(setListElement(new ArrayList<Object>((Collection<Object>) target), i, element,
                preprocessings));
        return target;
    }

    protected List<Object> setListElement(List<Object> list, int index, Object element, Preprocessing[] preprocessings) {
        if (list.size() > index) list.set(index, preprocess(element, preprocessings, 0));
        else for (int i = list.size(); i <= index; i++)
            list.add(i == index ? preprocess(element, preprocessings, 0) : null);
        return list;
    }

    public Object getElement(Object source, Object index) {
        if (source instanceof List) {
            List<Object> list = (List<Object>) source;
            return list.size() > (Integer) index ? list.get((Integer) index) : null;
        } else if (source instanceof Collection && ((Collection) source).size() > (Integer) index) {
            int i = 0;
            for (Object object : (Collection) source) if (i++ == (Integer) index) return object;
        }
        return null;
    }
}
