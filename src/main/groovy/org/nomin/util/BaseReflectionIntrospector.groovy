package org.nomin.util

import org.nomin.core.NominException
import java.lang.reflect.Method

/**
 * Contains general implementation for reflection introspectors.
 * @author Dmitry Dobrynin
 * Date: 23.11.2010 time: 8:21:13
 */
abstract class BaseReflectionIntrospector extends BaseIntrospector implements Introspector {
  public static final InstanceCreator instanceCreator = new ReflectionInstanceCreator()

  MethodInvocation invocation(String name, Class<?> targetClass, Object... args) {
    Method method = findApplicableMethod(name, targetClass, args)
    if (!method) throw new NominException("Could not find method ${targetClass.simpleName}.${name}(${args})!")
    new ReflectionMethodInvocation(method, args);
  }

  InstanceCreator instanceCreator() { instanceCreator }
}
