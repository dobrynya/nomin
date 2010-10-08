package org.nomin.core

/**
 * Tests ContextManager.
 * @author Dmitry Dobrynin
 * Created: 24.04.2010 17:04:39
 */
class ContextManagerTest {

  def cm = new ContextManager()
  def ownerProperty = "Just a value"

  @org.junit.Test
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

  @org.junit.Test(expected = RuntimeException.class)
  void testModification() {
    cm.a = "Not allowed"
  }

  @org.junit.Test(expected = RuntimeException.class)
  void testNonExistentObject() {
    cm.nonExistent
  }

  @org.junit.Test
  void testMethodMissing() {
    cm.pushLocalContext [:]
    cm.pushSharedContext function: { a, b, c -> "Function(${a}, ${b}, ${c})" }
    assert "Function(1, 2, 3)" == cm.function(1, 2, 3)
  }

  @org.junit.Test(expected = RuntimeException.class)
  void testNonExistentClosure() {
    cm.nonexistentClosure()
  }

  @org.junit.Test
  void testLookingUpOwnerPropety() {
    cm.pushLocalContext [:]
    def closure = { ownerProperty }
    closure = cm.makeContextAware(closure)
    assert ownerProperty == closure()
  }
}
