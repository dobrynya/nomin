package org.nomin.util

import java.lang.reflect.Field

/**
 * Uses fields to change state of an instance. It's useful when the class of the instance isn't satisfied to the JavaBeans
 * convention.
 * @author Dmitry Dobrynin
 * Created 16.04.2010 11:03:42
 */
class ExplodingIntrospector extends BaseReflectionIntrospector {
  PropertyAccessor createAccessor(String name, Class<?> targetClass) {
    def field = searchField(targetClass, name)
    if (field) new FieldPropertyAccessor(name, TypeInfoFactory.typeInfo(field.genericType), field)
  }

  Set<String> properties(Class<?> targetClass) {
    new HashSet(collectFields(targetClass).collect {it.name})
  }

  protected Field searchField(Class clazz, String fieldName) {
    def field = clazz.declaredFields.find { it.name == fieldName }
    field ?: clazz.superclass ? searchField(clazz.superclass, fieldName) : null
  }

  protected List<Field> collectFields(Class<?> targetClass) {
    def fields = targetClass != Object ? targetClass.declaredFields.findAll { !it.isSynthetic() } : []
    if (targetClass.superclass) fields + collectFields(targetClass.superclass)
    else fields
  }
}
