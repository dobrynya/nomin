package org.nomin.core;

import org.nomin.util.*;

import java.util.*;

import static java.text.MessageFormat.format;
import static java.util.Arrays.*;

/**
 * Provides access to collections.
 * @author Dmitry Dobrynin
 *         Created 28.04.2010 10:46:45
 */
@SuppressWarnings({"unchecked"})
public class CollectionRuleElem extends PropRuleElem {
    public CollectionRuleElem(PropertyAccessor property, TypeInfo typeInfo, InstanceCreator instanceCreator) {
        super(property, typeInfo, instanceCreator);
    }

    public Object set(Object instance, Object value) throws Exception {
        if (next == null) {
            Collection<?> source = (value == null ? null : asCollection(value));
            property.set(instance, source == null || source.size() == 0 ? null : containerHelper.convert(source, preprocessings));
        } else {
            Object ov = property.get(instance), nv = next.set(ov, value);
            if (ov != nv) property.set(instance, nv);
        }
        return instance;
    }

    protected Collection<?> asCollection(Object value) {
        if (value instanceof Object[]) return asList((Object[]) value);
        if (value instanceof Collection) return (Collection<Object>) value;
        if (value instanceof Map) return (Collection) ((Map<Object, Object>) value).entrySet();
        throw new NominException(format("Could not process not a collection/array value: {0}!", value));
    }

    public String toString() { return format("{0}:{1}", property.getName(), typeInfo); }
}
