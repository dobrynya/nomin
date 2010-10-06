package org.nomin.core

/**
 * Document please.
 * @author Dmitry Dobrynin
 * Created 09.04.2010 10:21:50
 */
class SeqPathElem extends PathElem {
  Integer index

  String toString() { "[${index}]" }
}
