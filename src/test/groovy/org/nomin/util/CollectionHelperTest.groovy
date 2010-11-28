package org.nomin.util

import org.nomin.core.ClassImprover
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.BlockingDeque
import java.util.concurrent.LinkedBlockingDeque
import org.nomin.core.NominException

/**
 * Tests CollectionHelper.
 * @author Dmitry Dobrynin
 * Date: 28.11.2010 time: 23:25:08
 */
class CollectionHelperTest {
  static { ClassImprover }

  @org.junit.Test
  void testCreateContainer() {
    assert new CollectionHelper(Collection[Entity]).createContainer(1) instanceof ArrayList
    assert new CollectionHelper(List[Entity]).createContainer(1) instanceof ArrayList
    assert new CollectionHelper(Set[Entity]).createContainer(1) instanceof HashSet
    assert new CollectionHelper(HashSet[Entity]).createContainer(1) instanceof HashSet
    assert new CollectionHelper(SortedSet[Entity]).createContainer(1) instanceof TreeSet
    assert new CollectionHelper(Queue[Entity]).createContainer(1) instanceof LinkedList
    assert new CollectionHelper(BlockingQueue[Entity]).createContainer(1) instanceof LinkedBlockingQueue
    assert new CollectionHelper(BlockingDeque[Entity]).createContainer(1) instanceof LinkedBlockingDeque

    try {
      new CollectionHelper(Map[Entity]).createContainer(1)
      assert false
    } catch (NominException ignored) {}

    try {
      new CollectionHelper(HashMap[Entity]).createContainer(1)
      assert false
    } catch (NominException ignored) {}
  }

  @org.junit.Test
  void testGetElement() {
    def ch = new CollectionHelper(List[Entity])
    assert ch.getElement(null, 0) == null && ch.getElement([], 1) == null && ch.getElement([1], 0) == 1

    def set = new HashSet()
    assert ch.getElement(set, 0) == null
    set = new HashSet([1])
    assert ch.getElement(set, 0) == 1
  }

  @org.junit.Test
  void testSetElement() {
    def val = new Object()
    def ch = new CollectionHelper(List[Entity])
    assert ch.setElement(null, 0, val, null) == [val]
    assert ch.setElement(null, 2, val, null) == [null, null, val]
    assert ch.setElement([null, null, null], 2, val, null) == [null, null, val]
    assert ch.setElement(null, -1, val, null) == [val]

    ch = new CollectionHelper(Set[Entity])
    assert [val].containsAll(ch.setElement(null, 0, val, null))
    assert [val].containsAll(ch.setElement(null, -1, val, null))
  }

  @org.junit.Test
  void testConvert() {
    def ch = new CollectionHelper(List[Entity])
    assert ch.convert([1]) == [1]
  }
}