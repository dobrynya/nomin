package org.nomin.util

/**
 * Generates PropertyAccessor implementation and creates its instance.
 * @author Dmitry Dobrynin
 * Created 24.05.2010 18:24:50
 */
class AsmJbIntrospector extends JbIntrospector {
  protected AsmPropertyAccessorGenerator generator = new AsmPropertyAccessorGenerator()
  protected namingPolicy = JbNamingPolicy.jbNamingPolicy

  PropertyAccessor property(String name, Class<?> targetClass) {
    def (getter, setter, typeInfo) = findAccessorMethods(namingPolicy.getters(name), namingPolicy.setters(name), targetClass)
    if (getter || setter) generator.generateAccessor(name, typeInfo, getter, setter)
  }
}
