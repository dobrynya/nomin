package org.nomin.util

import org.junit.*
import org.nomin.entity.Person
import static org.nomin.util.TypeInfoFactory.typeInfo
import org.nomin.core.ClassImprover

/**
 * Tests TypeInfo.
 * @author Dmitry Dobrynin
 * Created: 08.05.2010 15:38:21
 */
@SuppressWarnings("GroovyAssignabilityCheck")
class TypeInfoFactoryTest {
  List<Person> persons;
  List persons1;
  Map<String, Person> persons2;
  Person[] person3;

  @BeforeClass
  static void before() { ClassImprover.initialize() }

  @Test
  void testCreation() {
    def ti = typeInfo(String)
    assert ti && !ti.isDynamic() && !ti.array && !ti.collection && ti.type == String
    ti = typeInfo(this.class.getDeclaredField("persons").genericType)
    assert ti && ti.collection && ti.type == List && ti.parameters.size() == 1 && ti.parameters[0].type == Person
    ti = typeInfo(this.class.getDeclaredField("persons1").genericType)
    assert ti && ti.collection && ti.type == List && !ti.parameters
    ti = typeInfo(this.class.getDeclaredField("persons2").genericType)
    assert ti && ti.map && ti.type == Map && ti.parameters.size() == 2 && ti.parameters[0].type == String && ti.parameters[1].type == Person
    ti = typeInfo(this.class.getDeclaredField("person3").genericType)
    assert ti && ti.isContainer() && ti.array && ti.parameters?.size() == 1 && ti.parameters[0].type == Person
  }

  @Test
  void testMerge() {
    TypeInfo ti = List[Person]
    ti = ti.merge(List[{ Person }])
    assert ti && ti.parameters[0].type == Person && ti.parameters[0].dynamicType != null && ti.parameters[0].dynamicType() == Person
    ti = typeInfo(List)
    assert ti && !ti.parameters
    ti = ti.merge(List[Person])
    assert ti.parameters && ti.parameters.size() == 1 && ti.parameters[0].type == Person
    ti = typeInfo(Object)
    ti = ti.merge(typeInfo(Person))
    assert ti.type == Person
  }

  @Test
  void testGetDynamicType() {
    def c = { "just a closure" }
    TypeInfo ti = typeInfo(c)
    assert ti && ti.isDynamic() && ti.dynamicType == c
    ti = List[c]
    assert ti && ti.parameters[0].isDynamic() && ti.parameters[0].dynamicType == c
  }
}
