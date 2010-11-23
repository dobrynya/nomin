package org.nomin.util;

import java.util.Arrays;
import net.sf.cglib.reflect.FastMethod;
import static java.text.MessageFormat.format;

/**
 * Invokes a method using CGLIB fast method.
 * @author Dmitry Dobrynin
 *         Date: 23.11.2010 time: 11:07:48
 */
public class FastMethodInvocation implements MethodInvocation {
    private FastMethod method;
    private Object[] args;

    public FastMethodInvocation(FastMethod method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    public Object invoke(Object instance) throws Exception { return method.invoke(instance, args); }

    public TypeInfo getTypeInfo() { return TypeInfoFactory.typeInfo(method.getJavaMethod().getGenericReturnType()); }

    public String toString() { return format("{0}({1})", method.getName(), Arrays.toString(args)); }
}
