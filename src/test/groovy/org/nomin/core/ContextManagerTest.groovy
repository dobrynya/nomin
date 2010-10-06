package org.nomin.core

import org.junit.Test

/**
 * Tests ContextManager.
 * @author Dmitry Dobrynin
 * Created: 24.04.2010 17:04:39
 */
class ContextManagerTest {

  def cm = new ContextManager()
  def ownerProperty = "Just a value"

  @Test
  void testPushPopPeek() {
    cm.pushLocalContext a: "a"
    assert cm.a == "a"
    cm.pushLocalContext a: "b"
    assert cm.a == "b"
    cm.popLocalContext()
    assert cm.a == "a"
    cm.pushSharedContext sharedData: "Shared data"
    assert "Shared data" == cm.sharedData
  }

  @Test (expected = RuntimeException)
  void testModification() {
    cm.a = "Not allowed"
  }

  @Test (expected = RuntimeException)
  void testNonExistentObject() {
    cm.nonExistent
  }

  @Test
  void testMethodMissing() {
    cm.pushLocalContext [:]
    cm.pushSharedContext function: { a, b, c -> "Function(${a}, ${b}, ${c})" }
    assert "Function(1, 2, 3)" == cm.function(1, 2, 3)
  }

  @Test (expected = RuntimeException)
  void testNonExistentClosure() {
    cm.nonexistentClosure()
  }

  @Test
  void testLookingUpOwnerPropety() {
    cm.pushLocalContext [:]
    def closure = { ownerProperty }
    closure = cm.makeContextAware(closure)
    assert ownerProperty == closure()
  }
}
