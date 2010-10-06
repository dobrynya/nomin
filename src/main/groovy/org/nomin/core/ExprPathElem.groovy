package org.nomin.core

/**
 * Contains an expression or arbitrary value.
 * @author Dmitry Dobrynin
 * Created 09.04.2010 10:37:45
 */
class ExprPathElem extends PathElem {
  def expr

  String toString() { expr instanceof Closure ? "{ expression }" : String.valueOf(expr) }
}
