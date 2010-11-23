package org.nomin.util

import java.lang.reflect.Method

/**
 * Introspects a JavaBeans class. No matter whether specified class is a concrete class or interface.
 * ReflectionIntrospector is restricted to work only with public methods. It creates property accessors using the supplied
 * naming policy.
 * @author Dmitry Dobrynin
 * Created 14.04.2010 17:11:13
 */
class ReflectionIntrospector extends BaseReflectionIntrospector {
  protected NamingPolicy namingPolicy;

  ReflectionIntrospector(namingPolicy) { this.namingPolicy = namingPolicy; }

  PropertyAccessor property(String name, Class<?> targetClass) {
    def (getter, setter, typeInfo) = findAccessorMethods(namingPolicy.getters(name), namingPolicy.setters(name), targetClass)
    if (getter || setter) createAccessor(name, getter, setter, typeInfo)
  }

  public Set<String> properties(Class<?> targetClass) {
    new HashSet(targetClass.methods.collect {
      it.declaringClass != Object ? namingPolicy.propertyAccessor(it.name) : null
    }.findAll { it })
  }

  protected PropertyAccessor createAccessor(String name, Method getter, Method setter, TypeInfo typeInfo) {
    new ReflectionPropertyAccessor(name, getter, setter, typeInfo)
  }
}
