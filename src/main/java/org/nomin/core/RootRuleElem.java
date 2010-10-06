package org.nomin.core;

import java.text.MessageFormat;

import org.nomin.core.preprocessing.Preprocessing;

/**
 * Provides access to a root instance.
 * @author Dmitry Dobrynin
 *         Created: 27.04.2010 23:20:50
 */
public class RootRuleElem extends RuleElem {
    public RootRuleElem(TypeInfo typeInfo) {
        super(typeInfo);
    }

    public void set(Object instance, Object value, Preprocessing preprocessing) {
        if (next != null) next.set(instance, value, preprocessing);
        else preprocessing.preprocess(value, instance);
    }

    protected Object retrieve(Object instance) { return instance; }

    protected void store(Object instance, Object value, Preprocessing reprocessing) {}

    public String toString() { return MessageFormat.format("{0}", typeInfo); }
}
