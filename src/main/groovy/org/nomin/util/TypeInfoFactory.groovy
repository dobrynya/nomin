package org.nomin.util

import java.lang.reflect.ParameterizedType
import java.lang.reflect.TypeVariable
import java.lang.reflect.WildcardType
import org.nomin.core.NominException

/**
 * Constructs TypeInfo instances.
 * @author Dmitry Dobrynin
 * Date: 09.10.2010 Time: 22:02:40
 */
class TypeInfoFactory {
  static TypeInfo objectTypeInfo = new TypeInfo(Object)

  static TypeInfo typeInfo(type) {
    if (type == Object) return objectTypeInfo
    if (Class.isInstance(type))
      return type.isArray() ? new TypeInfo(type, [new TypeInfo(type.componentType)]) : new TypeInfo(type)

    switch (type) {
      case ParameterizedType: return new TypeInfo(type.rawType, type.actualTypeArguments.collect { typeInfo(it) })

      case WildcardType: return typeInfo(type.upperBounds ? type.upperBounds[0] :
        type.lowerBounds ? typeInfo(type.lowerBounds[0]) : Object)

      case TypeVariable: return typeInfo(type.bounds ? type.bounds[0] : Object)
      case Closure: return new TypeInfo(type)
    }
    throw new NominException("Could not recognize type ${type}!")
  }

  static TypeInfo typeInfo(type, params) {
    new TypeInfo(type,
            Collection.isInstance(params) ? params.collect { typeInfo(it) } : [typeInfo(params)])
  }

  static TypeInfo merge(TypeInfo original, TypeInfo specified) {
    List<TypeInfo> params = []
    for (i in 0..<[original.parameters.size(), specified.parameters.size()].max())
      params[i] = original.parameters[i] && specified.parameters[i] ?
        merge(original.parameters[i], specified.parameters[i]) : specified.parameters[i] ?: original.parameters[i]
    new TypeInfo(specified.type ?: original.type, specified.dynamicType ?: original.dynamicType, params)
  }
}
