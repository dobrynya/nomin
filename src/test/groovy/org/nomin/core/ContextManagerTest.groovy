package org.nomin.core

import org.junit.Test
import org.nomin.context.MapContext

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
    cm.pushLocal new MapContext(a: "a")
    assert cm.a == "a"
    cm.pushLocal new MapContext(a: "b")
    assert cm.a == "b"
    cm.popLocal()
    assert cm.a == "a"
    cm.replaceShared new MapContext(sharedData: "Shared data")
    assert "Shared data" == cm.sharedData
    cm.clearContexts()
    assert cm.localStack.isEmpty() && cm.globalContext.isEmpty()
  }

  @Test(expected = RuntimeException.class)
  void testModification() { cm.a = "Not allowed" }

  @Test(expected = RuntimeException.class)
  void testNonExistentObject() { cm.nonExistent }

  @Test
  void testMethodMissing() {
    cm.pushLocal new MapContext([:])
    cm.replaceShared new MapContext(function: { a, b, c -> "Function(${a}, ${b}, ${c})" })
    assert "Function(1, 2, 3)" == cm.function(1, 2, 3)
  }

  @Test(expected = RuntimeException.class)
  void testNonExistentClosure() {
    cm.nonexistentClosure()
  }

  @Test
  void testLookingUpOwnerPropety() {
    cm.pushLocal new MapContext([:])
    def closure = { ownerProperty }
    closure = cm.makeContextAware(closure)
    assert ownerProperty == closure()
  }
}
