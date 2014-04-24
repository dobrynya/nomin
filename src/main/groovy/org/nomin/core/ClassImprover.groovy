package org.nomin.core

import org.nomin.util.*
import org.slf4j.LoggerFactory

/**
 * Adds getAt method to Class.
 * @author Dmitry Dobrynin
 * Created 11.05.2010 16:44:23
 */
class ClassImprover {
  static void initialize() {
      LoggerFactory.getLogger(ClassImprover).debug("Adding getAt method to Class")
      Class.metaClass.getAt = { arg -> TypeInfoFactory.typeInfo(delegate, arg) }
  }
}
