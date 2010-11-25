package org.nomin.util

import org.nomin.core.NominException
import java.lang.reflect.Method

/**
 * Contains general implementation for reflection introspectors.
 * @author Dmitry Dobrynin
 * Date: 23.11.2010 time: 8:21:13
 */
abstract class BaseReflectionIntrospector extends BaseIntrospector {
  public static final InstanceCreator instanceCreator = new ReflectionInstanceCreator()

  protected MethodInvocation createInvocation(Method method, Object... args) {
    new ReflectionMethodInvocation(method, args)
  }

  InstanceCreator instanceCreator() { instanceCreator }
}
