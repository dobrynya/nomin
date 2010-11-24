package org.nomin.core;

import groovy.lang.Closure;
import org.nomin.util.TypeInfo;

/**
 * Calculates an expression.
 * @author Dmitry Dobrynin
 *         Created: 27.04.2010 22:45:55
 */
public class ClosureRuleElem extends RuleElem {
    final Closure closure;

    public ClosureRuleElem(Closure closure, TypeInfo typeInfo) {
        super(typeInfo);
        this.closure = closure;
    }

    public Object get(Object instance) throws Exception { return closure.call(); }

    public Object set(Object instance, Object value) throws Exception {
        throw new NominException(true, "Could not assign a value to the closure!");
    }

    public String toString() { return "{ expression }"; }
}
