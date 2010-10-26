package org.nomin.util

/**
 * Introspects a JavaBeans class. No matter whether specified class is a concrete class or interface.
 * JbIntrospector is restricted to work only with public methods.
 * @author Dmitry Dobrynin
 * Created 14.04.2010 17:11:13
 */
class JbIntrospector extends BaseIntrospector {
  private NamingPolicy namingPolicy = JbNamingPolicy.jbNamingPolicy

  PropertyAccessor property(String name, Class<?> targetClass) {
    def (getter, setter, typeInfo) = findAccessorMethods(namingPolicy.getters(name), namingPolicy.setters(name), targetClass)
    if (getter || setter) new JbPropertyAccessor(name, getter, setter, typeInfo)
  }

  public Set<String> properties(Class<?> targetClass) {
    new HashSet(targetClass.methods.collect {
      it.declaringClass != Object ? namingPolicy.propertyAccessor(it.name) : null
    }.findAll { it })
  }
}
