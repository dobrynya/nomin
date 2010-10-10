package org.nomin.core;

import org.nomin.util.PropertyAccessor;
import org.nomin.core.preprocessing.Preprocessing;
import org.nomin.util.TypeInfo;

import static java.text.MessageFormat.format;

/**
 * Provides access to a property.
 * @author Dmitry Dobrynin
 *         Created: 27.04.2010 20:57:15
 */
public class PropRuleElem extends RuleElem {
    final PropertyAccessor property;

    public PropRuleElem(TypeInfo typeInfo, PropertyAccessor property) {
        super(typeInfo);
        this.property = property;
    }

    protected Object retrieve(Object instance) {
        return property.get(instance);
    }

    protected void store(Object instance, Object value, Preprocessing preprocessing) {
        property.set(instance, preprocessing != null ? preprocessing.preprocess(value, null) : value);
    }

    Object createInstance(Object value) {
        try {
            return typeInfo.determineTypeDynamically(value).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not instantiate " + typeInfo.determineType().getSimpleName() + "!", e);
        }
    }

    protected Object initialize(Object instance, Object value) {
        Object result = createInstance(value);
        property.set(instance, result);
        return result;
    }

    public String toString() { return format("{0}:{1}", property.getName(), typeInfo); }
}
