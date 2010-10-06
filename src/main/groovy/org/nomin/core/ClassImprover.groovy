package org.nomin.core

/**
 * Adds getAt method to Class.
 * @author Dmitry Dobrynin
 * Created 11.05.2010 16:44:23
 */
class ClassImprover {
  static {
    Class.metaClass.getAt = {arg ->
      def params = []
      if (Collection.isInstance(arg)) arg.each { params << TypeInfo.typeInfo(it) }
      else params << TypeInfo.typeInfo(arg)
      new TypeInfo(delegate, params)
    }
  }
}
