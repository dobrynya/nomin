package org.nomin.util

import java.lang.reflect.Field
import org.nomin.core.TypeInfo

/**
 * Uses fields to change state of an instance. It's useful when the type of an instance isn't satisfied to JavaBeans
 * convention.
 * @author Dmitry Dobrynin
 * Created 16.04.2010 11:03:42
 */
class ExplodingIntrospector extends JbIntrospector {

  def PropertyAccessor property(String name, Class<?> targetClass) {
    def field = searchField(targetClass, name)
    new FieldPropertyAccessor(name, TypeInfo.typeInfo(field.genericType), field)
  }

  def Set<String> properties(Class<?> targetClass) {
    Set<String> properties = new HashSet()
    collectFields(targetClass).each { properties.add(it.name) }
    return properties;
  }

  protected Field searchField(Class clazz, String fieldName) {
    def field = clazz.declaredFields.find { it.name == fieldName }
    field ?: clazz.superclass ? searchField(clazz.superclass, fieldName) : null
  }

  protected List<Field> collectFields(Class<?> targetClass) {
    def fields = targetClass != Object ? targetClass.declaredFields.collect { it } : []
    if (targetClass.superclass) fields + collectFields(targetClass.superclass)
    fields
  }
}
