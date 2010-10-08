package org.nomin.core;

import org.nomin.core.preprocessing.Preprocessing;
import org.nomin.util.PropertyAccessor;
import java.util.*;
import java.util.concurrent.*;
import static java.text.MessageFormat.format;

/**
 * Provides access to collections.
 * @author Dmitry Dobrynin
 *         Created 28.04.2010 10:46:45
 */
@SuppressWarnings({"unchecked"})
public class CollectionRuleElem extends PropRuleElem {
    public CollectionRuleElem(TypeInfo typeInfo, PropertyAccessor property) {
        super(typeInfo, property);
    }

    Object createContainer(int size) {
        Class<?> clazz = property.getTypeInfo().type;
        if (clazz == Collection.class || clazz == List.class) return new ArrayList(size);
        else if (clazz == Set.class) return new HashSet(size);
        else if (clazz == SortedSet.class || clazz == NavigableSet.class) return new TreeSet();
        else if (clazz == Queue.class || clazz == Deque.class) return new LinkedList();
        else if (clazz == BlockingQueue.class) return new LinkedBlockingQueue(size);
        else if (clazz == BlockingDeque.class) return new LinkedBlockingDeque(size);
        else if (!clazz.isInterface() && Collection.class.isAssignableFrom(clazz)) {
            try { return clazz.newInstance(); }
            catch (Exception ignored) {}
        }
        throw new NominException(format("Could not instantiate {0}!", clazz.getName()));
    }

    Object createInstance(Object value) {
        int size = 0;
        if (value instanceof Collection) size = ((Collection) value).size();
        else if (value instanceof Object[]) size = ((Object[]) value).length;
        return createContainer(size);
    }

    protected void store(Object instance, Object value, Preprocessing preprocessing) {
        Collection<Object> result = (Collection<Object>) initialize(instance, value);
        // Optimizing performance: checking preprocessing only once
        if (preprocessing != null) for (Object o : iterable(value)) result.add(preprocessing.preprocess(o, null));
        else for (Object o : iterable(value)) result.add(o);
    }

    protected Iterable<Object> iterable(Object value) {
        if (value instanceof Collection) return (Iterable<Object>) value;
        else if (value instanceof Object[]) return Arrays.asList((Object[]) value);
        else return Collections.emptyList();
    }

    public String toString() { return format("{0}:{1}", property.getName(), typeInfo); }
}
