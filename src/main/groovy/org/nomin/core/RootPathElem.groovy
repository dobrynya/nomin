package org.nomin.core

/**
 * Specifies the root of a path.
 * @author Dmitry Dobrynin
 * Created 09.04.2010 10:36:08
 */
class RootPathElem extends PathElem {
  String rootPathElementSide
  Class<?> rootPathElementClass
  String toString() { "root" }
}
