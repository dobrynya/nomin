package org.nomin.core

import org.nomin.entity.Person
import org.junit.*

/**
 * Documentation.
 * @author Dmitry Dobrynin
 * Created: 08.05.2010 15:38:21
 */
@SuppressWarnings("GroovyAssignabilityCheck")
class TypeInfoTest {
  List<Person> persons;
  List persons1;
  Map<String, Person> persons2;
  Person[] person3;

  @Before
  void before() { ClassImprover }

  @Test
  void testCreation() {
    def ti = TypeInfo.typeInfo(String)
    assert ti && !ti.isDynamic() && !ti.isArray() && !ti.isCollection() && ti.type == String
    ti = TypeInfo.typeInfo(this.class.getDeclaredField("persons").genericType)
    assert ti && ti.isCollection() && ti.type == List && ti.parameters.size() == 1 && ti.parameters[0].type == Person
    ti = TypeInfo.typeInfo(this.class.getDeclaredField("persons1").genericType)
    assert ti && ti.isCollection() && ti.type == List && !ti.parameters
    ti = TypeInfo.typeInfo(this.class.getDeclaredField("persons2").genericType)
    assert ti && ti.type == Map && ti.parameters.size() == 2 && ti.parameters[0].type == String && ti.parameters[1].type == Person
    ti = TypeInfo.typeInfo(this.class.getDeclaredField("person3").genericType)
    assert ti && ti.isContainer() && ti.isArray() && ti.parameters?.size() == 1 && ti.parameters[0].type == Person
  }

  @Test
  void testMerge() {
    TypeInfo ti = List[Person]
    ti.merge List[{ Person }]
    assert ti && ti.parameters[0].type == Person && ti.parameters[0].dynamicType != null && ti.parameters[0].dynamicType() == Person
    ti = TypeInfo.typeInfo(List)
    assert ti && !ti.parameters
    ti.merge List[Person]
    assert ti.parameters && ti.parameters.size() == 1 && ti.parameters[0].type == Person
    ti = TypeInfo.typeInfo(Object)
    ti.merge TypeInfo.typeInfo(Person)
    assert ti.type == Person
  }

  @Test
  void testGetDynamicType() {
    def c = { "just a closure" }
    TypeInfo ti = TypeInfo.typeInfo(c)
    assert ti && ti.isDynamic() && ti.getDynamicType() == c
    ti = List[c]
    assert ti && ti.isDynamic() && ti.getDynamicType() == c
  }
}
