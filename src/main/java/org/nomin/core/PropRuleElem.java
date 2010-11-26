package org.nomin.core;

import org.nomin.util.*;
import static java.text.MessageFormat.format;
import static org.nomin.core.preprocessing.Preprocessing.preprocess;

/**
 * Provides access to a property.
 * @author Dmitry Dobrynin
 *         Created: 27.04.2010 20:57:15
 */
public class PropRuleElem extends RuleElem {
    protected final PropertyAccessor property;
    protected final InstanceCreator instanceCreator;

    public PropRuleElem(PropertyAccessor property, TypeInfo typeInfo, InstanceCreator instanceCreator) {
        super(typeInfo);
        this.property = property;
        this.instanceCreator = instanceCreator;
    }

    public Object get(Object instance) throws Exception {
        return instance == null ? null : next == null ? property.get(instance) : next.get(property.get(instance));
    }

    public Object set(Object instance, Object value) throws Exception {
        if (next == null) property.set(instance, preprocess(value, preprocessings, 0));
        else {
            Object ov = property.get(instance), nv =
                    next.set(ov == null ? instanceCreator.create(typeInfo.determineTypeDynamically(value)) : ov, value);
            if (ov != nv) property.set(instance, nv);
        }
        return instance;
    }

    public String toString() { return format("{0}:{1}", property.getName(), typeInfo); }
}
