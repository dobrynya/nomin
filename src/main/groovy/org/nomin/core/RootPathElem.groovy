package org.nomin.core

import org.nomin.util.TypeInfo

/**
 * Specifies the root of a path.
 * @author Dmitry Dobrynin
 * Created 09.04.2010 10:36:08
 */
class RootPathElem extends PathElem {
  String rootPathElementSide
  Class<?> rootPathElementClass

  RuleElem createMappingRuleElement(TypeInfo ownerTypeInfo, TypeInfo hint, RuleElem prev) {
    new RootRuleElem(ownerTypeInfo, pathElementMappingEntry.mapping.mapper, pathElementMappingEntry.mappingCase)
  }

  String toString() { nextPathElement ? "${rootPathElementSide}${nextPathElement}" : rootPathElementSide }
}
