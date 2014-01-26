package org.nomin.core

import org.nomin.util.*

/**
 * Adds getAt method to Class.
 * @author Dmitry Dobrynin
 * Created 11.05.2010 16:44:23
 */
class ClassImprover {
  static void initialize() {
    Class.metaClass.getAt = { arg -> TypeInfoFactory.typeInfo(delegate, arg) }
  }
}
