package org.nomin.core

import org.nomin.entity.Person
import org.nomin.util.TypeInfo

/**
 * Tests adding getAt method to the Class class.
 * @author Dmitry Dobrynin
 * Created 11.05.2010 16:47:25
 */
class ClassImproverTest {
  @org.junit.Before
  void before() { ClassImprover.initialize() }

  @org.junit.Test
  void test() {
    def ti = List[Person]
    assert ti && ti instanceof TypeInfo && ti.type == List && ti.parameters && ti.parameters.size() == 1 &&
            ti.parameters[0] instanceof TypeInfo && ti.parameters[0].type == Person
    ti = List[{ it instanceof String ? String : Integer }]
    assert ti && ti instanceof TypeInfo && ti.type == List && ti.parameters && ti.parameters.size() == 1 &&
            ti.parameters[0] instanceof TypeInfo && ti.parameters[0].dynamicType
    ti = Map[String, Person]
    assert ti && ti instanceof TypeInfo && ti.type == Map && ti.parameters && ti.parameters.size() == 2 &&
            ti.parameters[0] instanceof TypeInfo && ti.parameters[0].type == String &&
            ti.parameters[1] instanceof TypeInfo && ti.parameters[1].type == Person
  }

  @org.junit.Test
  void testerHintedMap() {
    def ti = Map[{ String }, { Person }]
    assert ti.parameters.size() == 2 && ti.parameters[0]?.dynamic && ti.parameters[1]?.dynamic
  }
}
