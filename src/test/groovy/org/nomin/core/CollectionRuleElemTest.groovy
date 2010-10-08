package org.nomin.core

import java.util.concurrent.BlockingDeque
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.LinkedBlockingQueue
import org.mockito.Mockito
import org.nomin.util.PropertyAccessor
import static org.nomin.core.TypeInfo.typeInfo

/**
 * Tests CollectionRuleElem.
 * @author Dmitry Dobrynin
 * Created 07.06.2010 20:08:33
 */
class CollectionRuleElemTest {
  PropertyAccessor pa = Mockito.mock(PropertyAccessor)
  CollectionRuleElem elem = new CollectionRuleElem(null, pa);

  @org.junit.Before
  void before() {
    Mockito.when(pa.getTypeInfo()).thenReturn(typeInfo(Collection), typeInfo(List), typeInfo(Set), typeInfo(SortedSet),
            typeInfo(NavigableSet), typeInfo(Queue), typeInfo(Deque), typeInfo(BlockingQueue), typeInfo(BlockingDeque),
            typeInfo(ArrayList), typeInfo(HashSet))
  }

  @org.junit.Test
  void testCreateContainer() {
    assert elem.createContainer(0) instanceof ArrayList
    assert elem.createContainer(0) instanceof ArrayList
    assert elem.createContainer(0) instanceof HashSet
    assert elem.createContainer(0) instanceof TreeSet
    assert elem.createContainer(0) instanceof TreeSet
    assert elem.createContainer(0) instanceof LinkedList
    assert elem.createContainer(0) instanceof LinkedList
    assert elem.createContainer(1) instanceof LinkedBlockingQueue
    assert elem.createContainer(1) instanceof LinkedBlockingDeque
    assert elem.createContainer(0) instanceof ArrayList
    assert elem.createContainer(0) instanceof HashSet
  }
}
