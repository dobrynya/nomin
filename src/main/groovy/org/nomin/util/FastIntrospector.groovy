package org.nomin.util

import java.lang.reflect.Method

/**
 * Creates property accessors and method invocations which use CGLIB to provide needed functionality.
 * @author Dmitry Dobrynin
 * Date: 23.11.2010 time: 10:29:18
 */
class FastIntrospector extends ReflectionIntrospector {
  private static final InstanceCreator instanceCreator = new FastInstanceCreator()

  FastIntrospector() { super(JbNamingPolicy.jbNamingPolicy) }

  FastIntrospector(policy) { super(policy) }

  InstanceCreator instanceCreator() { instanceCreator }

  protected PropertyAccessor createAccessor(String name, Method getter, Method setter, TypeInfo typeInfo) {
    new FastPropertyAccessor(name,
            getter ? FastHelper.getOrCreateFastClass(getter.getDeclaringClass()).getMethod(getter) : null,
            setter ? FastHelper.getOrCreateFastClass(getter.getDeclaringClass()).getMethod(setter) : null,
            typeInfo)
  }

  protected MethodInvocation createInvocation(Method method, Object... args) {
    new FastMethodInvocation(FastHelper.getOrCreateFastClass(method.getDeclaringClass()).getMethod(method), args)
  }
}
