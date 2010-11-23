package org.nomin.util;

/**
 * Invokes a method.
 * @author Dmitry Dobrynin
 *         Created: 18.04.2010 14:55:24
 */
public interface MethodInvocation {
    Object invoke(Object instance);

    TypeInfo getTypeInfo();
}
