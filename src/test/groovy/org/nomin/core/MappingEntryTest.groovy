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
    assert !me.completed()
    me.pathElem new RootPathElem()
    assert !me.completed()
    me.pathElem new RootPathElem()
    assert me.completed()
  }
}
