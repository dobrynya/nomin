package org.nomin.core;

import groovy.lang.Closure;
import org.nomin.core.preprocessing.Preprocessing;
import org.nomin.util.TypeInfoFactory;

/**
 * Calculates an expression.
 * @author Dmitry Dobrynin
 *         Created: 27.04.2010 22:45:55
 */
public class ClosureRuleElem extends RuleElem {
    final Closure closure;

    public ClosureRuleElem(Closure closure) {
        super(TypeInfoFactory.typeInfo(Undefined.class));
        this.closure = closure;
    }

    protected Object retrieve(Object instance) { return closure.call(); }

    protected void store(Object instance, Object value, Preprocessing reprocessing) {}

    public void set(Object instance, Object value, Preprocessing preprocessing) {}

    public String toString() { return "{ expression }"; }
}
