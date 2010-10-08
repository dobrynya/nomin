package org.nomin.core

/**
 * Contains the index in a sequence or the key in case of Map.
 * @author Dmitry Dobrynin
 * Created 09.04.2010 10:21:50
 */
class SeqPathElem extends PathElem {
  Object index

  String toString() { "[${index}]" }
}
