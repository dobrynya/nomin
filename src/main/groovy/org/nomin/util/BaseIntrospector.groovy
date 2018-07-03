package org.nomin.util

import java.lang.reflect.Method
import org.nomin.core.NominException

/**
 * Provides base operations to introspect classes.
 * @author Dmitry Dobrynin
 * Created 24.05.2010 12:50:52
 */
@SuppressWarnings("GroovyAssignabilityCheck") abstract
class BaseIntrospector implements Introspector {
  protected Map pacache = [:]

  MethodInvocation invocation(String name, Class<?> targetClass, Object... args) {
    Method method = findApplicableMethod(name, targetClass, args)
    if (method) createInvocation(method, args) else null
  }

    PropertyAccessor property(String name, Class<?> targetClass) {
    PropertyAccessor pa = pacache[[targetClass, name]]
    if (!pa) {
      pacache[[targetClass, name]] = (pa = createAccessor(name, targetClass))
    }
    pa
  }

  protected abstract MethodInvocation createInvocation(Method method, Object... args);

  protected abstract PropertyAccessor createAccessor(String name, Class<?> targetClass);

  protected Method findApplicableMethod(String name, Class targetClass, Object... args) {
    List<Class> argTypes = args.collect { it?.class }
    targetClass.methods.find {
      if (name == it.name && it.parameterTypes.length == argTypes.size()) {
        argTypes.eachWithIndex { Class argType, index ->
          if (!canApply(argType, it.parameterTypes[index])) return null
        }
        return it
      }
    }
  }

  protected boolean canApply(Class argType, Class paramType) {
    (!argType || argType == paramType || paramType.isAssignableFrom(argType)) ||
            (paramType.isPrimitive() && !argType.isPrimitive() &&
                    argType.fields.find { it.name == "TYPE" }?.get() == paramType)
  }
  
  protected List findAccessorMethods(List<String> getterNames, List<String> setterNames, Class targetClass) {
    Method getter = targetClass.methods.find { getterNames.contains(it.name) && !it.genericParameterTypes }
    def getterType = getter?.genericReturnType
    Method setter = targetClass.methods.find {
      setterNames.contains(it.name) && (!getterType || it.genericParameterTypes[0] == getterType)
    }
    [getter, setter, getterType ? TypeInfoFactory.typeInfo(getterType) :
      setter?.genericParameterTypes ? TypeInfoFactory.typeInfo(setter.genericParameterTypes[0]) : null]
  }
}
