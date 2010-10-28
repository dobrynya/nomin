package org.nomin.core

import org.nomin.util.TypeInfo

/**
 * Contains an expression or arbitrary value.
 * @author Dmitry Dobrynin
 * Created 09.04.2010 10:37:45
 */
class ExprPathElem extends PathElem {
  def exprPathElementExpr

  RuleElem createMappingRuleElement(TypeInfo typeInfo, RuleElem prev) {
    Closure.isInstance(exprPathElementExpr) ? new ClosureRuleElem(exprPathElementExpr) : new ValueRuleElem(exprPathElementExpr);
  }

  String toString() { Closure.isInstance(exprPathElementExpr) ? "{ expression }" : exprPathElementExpr }
}
