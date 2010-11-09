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
    public CollectionRuleElem(PropertyAccessor property, TypeInfo typeInfo) { super(property, typeInfo); }

    public Object set(Object instance, Object value) throws Exception {
        if (instance == null) instance = property.newOwner();
        if (next == null) {
            Collection<Object> source = asCollection(value);
            property.set(instance, source == null || source.size() == 0 ? null : containerHelper.convert(source, preprocessings));
        } else
            property.set(instance, next.set(property.get(instance), value));
        return instance;
    }

    protected Collection<Object> asCollection(Object value) {
        if (Object[].class.isInstance(value)) return asList((Object[]) value);
        if (Collection.class.isInstance(value)) return (Collection<Object>) value;
        if (Map.class.isInstance(value)) return (Collection) ((Map<Object, Object>) value).entrySet();
        return null;
    }

    public String toString() { return format("{0}:{1}", property.getName(), typeInfo); }
}
