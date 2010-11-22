package org.nomin.core

import org.nomin.entity.*
import static org.mockito.Mockito.*
import org.nomin.util.*

/**
 * Tests all the rule elements.
 * @author Dmitry Dobrynin
 * Date: 22.10.2010 Time: 9:10:24
 */
class RuleElemTest implements MappingConsts {
  InstanceCreator ic = mock(InstanceCreator)
  PropertyAccessor pa = mock(PropertyAccessor)
  RuleElem next = mock(RuleElem)
  Object instance = new Object()
  Object result = new Object()
  Object nextResult = new Object()
  Object value = new Object()

  @org.junit.Test
  void testPropElemGet() {
    PropRuleElem propElem = new PropRuleElem(pa, TypeInfoFactory.typeInfo(Details), ic)
    verifyZeroInteractions(pa)

    when(pa.get(instance)).thenReturn(result)
    assert propElem.get(null) == null && propElem.get(instance) == result

    propElem.next = next
    when(next.get(result)).thenReturn(nextResult)
    assert propElem.get(instance) == nextResult
  }

  @org.junit.Test
  void testPropElemSet() {
    PropRuleElem propElem = new PropRuleElem(pa, TypeInfoFactory.typeInfo(Details), ic)
    assert instance == propElem.set(instance, result)
    verify(pa).set(instance, result)

    propElem.next = next
    when(pa.get(instance)).thenReturn(result)
    when(next.set(result, value)).thenReturn(result)
    doThrow(new RuntimeException()).when(pa).set(instance, result)
    assert propElem.set(instance, value) == instance
    when(next.set(result, value)).thenReturn(nextResult)
    assert propElem.set(instance, value) == instance
    verify(pa).set(instance, nextResult)

    when(ic.create(Details)).thenReturn(result)
    when(pa.get(instance)).thenReturn(null)
    assert propElem.set(instance, value) == instance
    verify(pa).set(instance, result)
  }

  @org.junit.Test
  void testCollectionElemSet() {
    CollectionRuleElem collElem = new CollectionRuleElem(pa, TypeInfoFactory.typeInfo(List), ic)

    assert collElem.asCollection([1, 2, 3] as Object[]) == [1, 2, 3]
    assert collElem.asCollection([1, 2, 3]) == [1, 2, 3]
    Set set = collElem.asCollection([a : 1, b: 2])
    for (Map.Entry e : set)
      assert (e.getKey() == "a" && e.getValue() == 1) || (e.getKey() == "b" && e.getValue() == 2)

    try { collElem.asCollection("Not collection!"); assert false }
    catch (NominException e) {
      assert e.getMessage().startsWith("Could not process not a collection/array value")
    }

    assert collElem.set(instance, null) == instance
    assert collElem.set(instance, []) == instance
    verify(pa, times(2)).set instance, null

    assert collElem.set(instance, [1, 2, 3]) == instance
    verify(pa).set instance, [1, 2, 3]

    collElem.next = next
    when(pa.get(instance)).thenReturn(null)
    when(pa.get(instance)).thenReturn(null)
    when(next.set(null, value)).thenReturn(nextResult)
    assert collElem.set(instance, value) == instance
    verify(pa).set(instance, nextResult)

    when(pa.get(instance)).thenReturn(result)
    when(next.set(result, value)).thenReturn(result)
    assert collElem.set(instance, value) == instance
    verify(pa).set(instance, nextResult)
  }

  @org.junit.Test
  void testSeqElemGet() {
    ContainerHelper ch = ContainerHelper.create(List[Person])
    SeqRuleElem seq = new SeqRuleElem(0, ch.elementType, ch, ic)
    assert seq.get(null) == null
    assert seq.get([1, 2, 3]) == 1
    seq.next = next
    when(next.get(1)).thenReturn(11)
    assert seq.get([1, 2, 3]) == 11
  }

  @org.junit.Test
  void testSeqElemSet() {
    ContainerHelper ch = ContainerHelper.create(List[Person])
    SeqRuleElem seq = new SeqRuleElem(0, ch.elementType, ch, ic)
    assert seq.set(null, value) == [value]

    seq.next = next
    when(ic.create(Person)).thenReturn(result)
    when(next.set(result, value)).thenReturn(result)
    assert seq.set(null, value) == [result]

    when(next.set(result, value)).thenReturn(result)
    assert seq.set([result], value) == [result]

    when(next.set(result, value)).thenReturn(nextResult)
    assert seq.set([result], value) == [nextResult]
  }

  static { ClassImprover }
}
