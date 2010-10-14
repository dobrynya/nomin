package org.nomin.core

import org.nomin.util.TypeInfo

/**
 * @author Dmitry Dobrynin
 * Date: 14.10.2010 Time: 23:03:09
 */
class MappingSide {
  PathElem pathElem
  RuleElem firstRuleElem
  RuleElem lastRuleElem
  Closure conversion
  List<TypeInfo> hints = []

  boolean isSide(String side) {
    RootPathElem.isInstance(pathElem) && pathElem.rootPathElementSide == side
  }

  boolean isSideA() { RootPathElem.isInstance(pathElem) && pathElem.rootPathElementSide == "a" }

  boolean isSideB() { RootPathElem.isInstance(pathElem) && pathElem.rootPathElementSide == "b" }
}
