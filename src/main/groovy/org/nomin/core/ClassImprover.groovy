package org.nomin.core

import org.nomin.util.TypeInfoFactory

/**
 * Adds getAt method to Class.
 * @author Dmitry Dobrynin
 * Created 11.05.2010 16:44:23
 */
class ClassImprover {
  static {
    Class.metaClass.getAt = { arg -> TypeInfoFactory.typeInfo(delegate, arg) }
  }
}
