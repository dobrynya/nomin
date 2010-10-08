package org.nomin.core

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

/**
 * Document please.
 * @author Dmitry Dobrynin
 * Created 12.04.2010 18:46:00
 */
class MappingEntryTest {

  def me = new MappingEntry()

  @org.junit.Test
  void testCompleted() {
    assertFalse me.completed()
    me.side.a.pathElem = new RootPathElem()
    assertFalse me.completed()
    me.side.b.pathElem = new RootPathElem()
    assertTrue me.completed()
  }
}
