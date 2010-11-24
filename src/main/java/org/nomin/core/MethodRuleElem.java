package org.nomin.core;

import org.nomin.util.MethodInvocation;
import org.nomin.util.TypeInfo;

/**
 * Invokes a method.
 * @author Dmitry Dobrynin
 *         Created: 29.04.2010 1:32:44
 */
public class MethodRuleElem extends RuleElem {
    final MethodInvocation invocation;

    public MethodRuleElem(TypeInfo typeInfo, MethodInvocation invocation) {
        super(typeInfo);
        this.invocation = invocation;
    }

    public Object get(Object instance) throws Exception {
        return next != null && instance != null ? next.get(invocation.invoke(instance)) :
                instance != null ? invocation.invoke(instance) : null;
    }

    public Object set(Object instance, Object value) throws Exception {
        throw new NominException(true, "Could not assign a value to the method invocation!");
    }

    public String toString() { return invocation.toString(); }
}
