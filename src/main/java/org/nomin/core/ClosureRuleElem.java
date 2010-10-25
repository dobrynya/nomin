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

    public Object get(Object instance) throws Exception { return closure.call(); }

    public Object set(Object instance, Object value) throws Exception {
        throw new NominException("Could not set the closure!");
    }

    public String toString() { return "{ expression }"; }
}
