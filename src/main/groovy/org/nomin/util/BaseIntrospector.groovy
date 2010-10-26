package org.nomin.util

import java.lang.reflect.Method
import org.nomin.core.NominException

/**
 * Provides base operations to introspect classes.
 * @author Dmitry Dobrynin
 * Created 24.05.2010 12:50:52
 */
@SuppressWarnings("GroovyAssignabilityCheck")
abstract class BaseIntrospector implements Introspector {
  MethodInvocation invocation(String name, Class<?> targetClass, Object... args) {
    Method method = findApplicableMethod(name, targetClass, args)
    if (!method) throw new NominException("Could not find method ${targetClass.simpleName}.${name}(${args})!")
    return new MethodInvocation(method, args);
  }

  protected boolean canApply(Class argType, Class paramType) {
    (!argType || argType == paramType || paramType.isAssignableFrom(argType)) ||
            (paramType.isPrimitive() && !argType.isPrimitive() &&
                    argType.fields.find { it.name == "TYPE" }?.get() == paramType)
  }

  protected Method findApplicableMethod(String name, Class targetClass, Object... args) {
    List<Class> argTypes = args.collect { it?.class }
    targetClass.methods.find {
      if (name == it.name && it.parameterTypes.length == argTypes.size()) {
        def applicable = true
        argTypes.eachWithIndex {Class argType, index ->
          if (!canApply(argType, it.parameterTypes[index])) {
            applicable = false
          }
        }
        applicable
      }
    }
  }
  
  protected List findAccessorMethods(List<String> getterNames, List<String> setterNames, Class targetClass) {
    Method getter = targetClass.methods.find { getterNames.contains(it.name) && !it.genericParameterTypes}
    def getterType = getter?.genericReturnType
    Method setter = targetClass.methods.find {
      setterNames.contains(it.name) && it.genericParameterTypes && (!getterType || it.genericParameterTypes[0] == getterType)
    }
    [getter, setter, getterType ? TypeInfoFactory.typeInfo(getterType) :
      setter?.genericParameterTypes ? TypeInfoFactory.typeInfo(setter.genericParameterTypes[0]) : null]
  }
}
