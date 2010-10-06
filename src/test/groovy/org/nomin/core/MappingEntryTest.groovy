package org.nomin.core

import org.junit.Test
import static org.junit.Assert.*

import org.nomin.core.RootPathElem
import org.nomin.core.MappingEntry

/**
 * Document please.
 * @author Dmitry Dobrynin
 * Created 12.04.2010 18:46:00
 */
class MappingEntryTest {

  def me = new MappingEntry()

  @Test
  void testCompleted() {
    assertFalse me.completed()
    me.side.a.pathElem = new RootPathElem()
    assertFalse me.completed()
    me.side.b.pathElem = new RootPathElem()
    assertTrue me.completed()
  }
}
