package org.nomin.core

import org.nomin.entity.Person

/**
 * Document please.
 * @author Dmitry Dobrynin
 * Created 11.05.2010 16:47:25
 */
class ClassImproverTest {
  @org.junit.Before
  void before() { ClassImprover }

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
}
