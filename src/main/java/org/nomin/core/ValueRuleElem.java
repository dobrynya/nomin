package org.nomin.core;

import org.nomin.core.preprocessing.Preprocessing;

/**
 * Contains static value and provides access to it.
 * @author Dmitry Dobrynin
 *         Created: 27.04.2010 22:43:38
 */
public class ValueRuleElem extends RuleElem {
    final Object value;

    public ValueRuleElem(Object value) {
        super(TypeInfo.typeInfo(value != null ? value.getClass() : Object.class));
        this.value = value;
    }

    protected Object retrieve(Object instance) { return value; }

    protected void store(Object instance, Object value, Preprocessing reprocessing) {}

    public void set(Object instance, Object value, Preprocessing preprocessing) {}

    public String toString() { return String.valueOf(value); }
}
