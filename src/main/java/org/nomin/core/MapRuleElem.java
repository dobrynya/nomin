package org.nomin.core;

import org.nomin.util.PropertyAccessor;

import java.util.HashMap;
import java.util.Map;

import static java.text.MessageFormat.format;

/**
 * Provides access to a property in case of Map.
 * @author Dmitry Dobrynin
 *         Date: 08.10.2010 Time: 9:57:22
 */
public class MapRuleElem extends PropRuleElem {
    public MapRuleElem(TypeInfo typeInfo, PropertyAccessor property) {
        super(typeInfo, property);
    }

    Object createInstance(Object value) {
        if (typeInfo.type == Map.class) return new HashMap<Object, Object>();
        else if (!typeInfo.type.isInterface()) {
            try { return typeInfo.type.newInstance(); }
            catch (Exception ignored) {}
        }
        throw new NominException(format("Could not instantiate {0}!", typeInfo.type.getName()));
    }
}
