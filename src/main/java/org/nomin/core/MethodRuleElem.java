package org.nomin.core;

import org.nomin.util.MethodInvocation;
import org.nomin.core.preprocessing.Preprocessing;
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

    protected Object retrieve(Object instance) {
        return invocation.invoke(instance);
    }

    protected void store(Object instance, Object value, Preprocessing preprocessing) {}

    public String toString() {
        return invocation.toString();
    }
}
