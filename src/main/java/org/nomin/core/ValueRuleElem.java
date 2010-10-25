package org.nomin.core;

import org.nomin.core.preprocessing.Preprocessing;
import org.nomin.util.TypeInfoFactory;

/**
 * Contains static value and provides access to it.
 * @author Dmitry Dobrynin
 *         Created: 27.04.2010 22:43:38
 */
public class ValueRuleElem extends RuleElem {
    final Object value;

    public ValueRuleElem(Object value) {
        super(TypeInfoFactory.typeInfo(value != null ? value.getClass() : Object.class));
        this.value = value;
    }

    public Object get(Object instance) throws Exception { return value; }

    public Object set(Object instance, Object value) throws Exception {
        throw new NominException("Could not change the constant value!");
    }

    public String toString() { return String.valueOf(value); }
}
