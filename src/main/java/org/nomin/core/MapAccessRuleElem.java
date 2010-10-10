package org.nomin.core;

import org.nomin.core.preprocessing.Preprocessing;
import org.nomin.util.TypeInfo;

import java.util.Map;

import static java.text.MessageFormat.*;

/**
 * Provides access to a map element with specified key.
 * @author Dmitry Dobrynin
 * Date: 07.10.2010 Time: 21:46:07
 */
public class MapAccessRuleElem extends RuleElem {
    private Object key;

    public MapAccessRuleElem(TypeInfo typeInfo, Object key) {
        super(typeInfo);
        this.key = key;
    }

    protected Object retrieve(Object instance) { return ((Map<Object, Object>) instance).get(key); }

    protected void store(Object instance, Object value, Preprocessing preprocessing) {
        ((Map<Object, Object>) instance).put(key, preprocessing != null ? preprocessing.preprocess(value, null) : value);
    }

    public String toString() { return format("[{0}]:{1}", key, typeInfo); }
}
