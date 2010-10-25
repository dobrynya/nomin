package org.nomin.util;

import org.nomin.core.NominException;
import org.nomin.core.preprocessing.Preprocessing;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import static java.text.MessageFormat.format;

/**
* @author Dmitry Dobrynin
*         Date: 25.10.2010 Time: 21:53:39
*/
@SuppressWarnings({"unchecked"})
public class CollectionHelper extends ContainerHelper {
    public CollectionHelper(TypeInfo typeInfo) {
        super(typeInfo.getType(), typeInfo.getParameter(0));
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
        throw new NominException(format("Could not instantiate {0}!", containerClass.getName()));
    }

    public Collection<Object> convert(Collection<Object> source, Preprocessing preprocessing) throws Exception {
        Collection<Object> target = createContainer(source.size());
        for (Object object : source)
            target.add(preprocessing != null ? preprocessing.preprocess(object) : object);
        return target;
    }

    public Object setElement(Object target, Object index, Object element, Preprocessing preprocessing) throws Exception {
        if (target == null) target = createContainer((Integer) index + 1);
        if (List.class.isInstance(target)) return setListElement((List<Object>) target, (Integer) index, element, preprocessing);
        List<Object> list = new ArrayList<Object>((Collection<Object>) target);
        ((Collection<Object>) target).clear();
        ((Collection<Object>) target).addAll(setListElement(list, (Integer) index, element, preprocessing));
        return target;
    }

    protected List<Object> setListElement(List<Object> list, int index, Object element, Preprocessing preprocessing) {
        if (list.size() > (Integer) index)
            list.set((Integer) index, preprocessing != null ? preprocessing.preprocess(element) : element);
        else for (int i = list.size(); i <= (Integer) index; i++)
            list.add(i == index ? (preprocessing != null ? preprocessing.preprocess(element) : element): null);
        return list;
    }

    public Object getElement(Object source, Object index) {
        if (List.class.isInstance(source)) {
            List<Object> list = (List<Object>) source;
            return list.size() > (Integer) index ? list.get((Integer) index) : null;
        } else if (Collection.class.isInstance(source) && ((Collection) source).size() > (Integer) index) {
            int i = 0;
            for (Object object : (Collection) source) if (i++ == (Integer) index) return object;
        }
        return null;
    }
}
