package org.nomin.core

import org.nomin.util.TypeInfo
import org.nomin.util.MethodInvocation

/**
 * Contains name of a method and invocation arguments.
 * @author Dmitry Dobrynin
 * Created 09.04.2010 10:25:07
 */
class MethodPathElem extends PathElem {
  String methodPathElementMethodName
  List methodPathElementInvocationParameters

  RuleElem createMappingRuleElement(TypeInfo ownerTypeInfo, TypeInfo hint, RuleElem prev) {
    MethodInvocation invocation = pathElementMappingEntry.mapping.introspector
            .invocation(methodPathElementMethodName, ownerTypeInfo.type, *methodPathElementInvocationParameters)
    new MethodRuleElem(invocation.typeInfo.merge(hint), invocation)
  }

  String toString() {
    nextPathElement ? ".${methodPathElementMethodName}(${methodPathElementInvocationParameters})${nextPathElement}" :
      ".${methodPathElementMethodName}(${methodPathElementInvocationParameters})"
  }
}
