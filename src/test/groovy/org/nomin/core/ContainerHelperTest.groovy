package org.nomin.core

import java.util.concurrent.*
import org.nomin.util.*
import static org.nomin.util.ContainerHelper.*

/**
 * Tests ContainerHelper and its factory.
 * @author Dmitry Dobrynin
 * Created 07.06.2010 20:08:33
 */
class ContainerHelperTest {
  static { ClassImprover }

  @org.junit.Test
  void testFactory() {
    assert ArrayHelper.isInstance(create(Array[Object]))
    assert CollectionHelper.isInstance(create(List[Object]))
    assert MapHelper.isInstance(create(Map[Object]))
  }


  @org.junit.Test
  void testCreateCollection() {
    assert ArrayList.isInstance(create(Collection[Object]).createContainer(0))
    assert ArrayList.isInstance(create(List[Object]).createContainer(0))
    assert ArrayList.isInstance(create(ArrayList[Object]).createContainer(0))
    assert HashSet.isInstance(create(Set[Object]).createContainer(0))
    assert TreeSet.isInstance(create(SortedSet[Object]).createContainer(0))
    assert TreeSet.isInstance(create(NavigableSet[Object]).createContainer(0))
    assert LinkedList.isInstance(create(Queue[Object]).createContainer(0))
    assert LinkedList.isInstance(create(Deque[Object]).createContainer(0))
    assert LinkedBlockingQueue.isInstance(create(BlockingQueue[Object]).createContainer(0))
    assert LinkedBlockingDeque.isInstance(create(BlockingDeque[Object]).createContainer(0))
  }

  @org.junit.Test
  void testCreateArray() {
    def array = create(Array[Object]).createContainer(5)
    assert array instanceof Object[] && array.length == 5
  }

  @org.junit.Test
  void testCreateMap() {
    assert HashMap.isInstance(create(Map[Object, Object]).createContainer(0))
    assert Hashtable.isInstance(create(Hashtable[Object, Object]).createContainer(0))
  }


}
