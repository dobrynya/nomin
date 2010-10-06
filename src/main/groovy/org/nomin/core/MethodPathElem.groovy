package org.nomin.core

/**
 * Contains name of a method and invocation arguments.
 * @author Dmitry Dobrynin
 * Created 09.04.2010 10:25:07
 */
class MethodPathElem extends PathElem {
  String methodName
  List params

  String toString() { "${methodName}(${params})" }
}
