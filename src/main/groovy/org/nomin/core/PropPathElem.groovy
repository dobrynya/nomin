package org.nomin.core

/**
 * Contains name of the requested property.
 * @author Dmitry Dobrynin
 * Created 09.04.2010 10:45:49
 */
class PropPathElem extends PathElem {
  String propPathElementPropertyName

  String toString() { nextPathElem ?
    ".${propPathElementPropertyName}${nextPathElem}" : ".${propPathElementPropertyName}"
  }
}
