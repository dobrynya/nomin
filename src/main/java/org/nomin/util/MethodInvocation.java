package org.nomin.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.nomin.core.*;
import static java.text.MessageFormat.format;

/**
 * Holds method invocation related data, i.e. a method to invoke and invocation arguments.
 * @author Dmitry Dobrynin
 *         Created: 18.04.2010 14:55:24
 */
public class MethodInvocation {
    private Method method;
    private Object[] args;

    public MethodInvocation(Method method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    public Object invoke(Object instance) {
        try { return method.invoke(instance, args); }
        catch (Exception e) { throw new NominException(format("Could not invoke {0} on {1}!", method, instance), e); }
    }

    public TypeInfo getTypeInfo() { return TypeInfoFactory.typeInfo(method.getGenericReturnType()); }

    public String toString() { return format("{0}({1})", method.getName(), Arrays.toString(args)); }
}
