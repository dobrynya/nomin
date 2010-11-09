package org.nomin.core;

import org.nomin.util.PropertyAccessor;
import org.nomin.util.TypeInfo;

import static java.text.MessageFormat.format;
import static org.nomin.core.preprocessing.PreprocessingHelper.preprocess;

/**
 * Provides access to a property.
 * @author Dmitry Dobrynin
 *         Created: 27.04.2010 20:57:15
 */
public class PropRuleElem extends RuleElem {
    final PropertyAccessor property;

    public PropRuleElem(PropertyAccessor property, TypeInfo typeInfo) {
        super(typeInfo);
        this.property = property;
    }

    public Object get(Object instance) throws Exception {
        return instance == null ? null : next == null ? property.get(instance) : next.get(property.get(instance));
    }

    public Object set(Object instance, Object value) throws Exception {
        if (instance == null) instance = property.newOwner();
        if (next == null) property.set(instance, preprocess(value, preprocessings, 0));
        else property.set(instance, next.set(property.get(instance), value));
        return instance;
    }

    public String toString() { return format("{0}:{1}", property.getName(), typeInfo); }
}
