package org.nomin.util

import org.junit.Test
import static org.junit.Assert.*

import org.nomin.entity.Child
import org.nomin.entity.Person
import org.nomin.entity.LegacyDetails

/**
 * Tests JbIntrospector.
 * @author Dmitry Dobrynin
 * Created 14.04.2010 17:51:18
 */
@SuppressWarnings("GroovyVariableNotAssigned")
class JbIntrospectorTest {
  def intr = new JbIntrospector()

  String methodToFind(String s, int i, String s2, long l) {
    "${s} ${i} ${s2} ${l}"
  }

  @Test
  void testInvocation() {
    def params = ["String", 1, null, 1L]
    def invocation = intr.invocation("methodToFind", getClass(), "String", 1, null, 1L)
    assert invocation && invocation.invoke(this) == "String 1 null 1"
  }

  @Test
  void testCanApply() {
    assert intr.canApply(null, String)
    assert intr.canApply(String, String)
    assert intr.canApply(Class.getPrimitiveClass("int"), Class.getPrimitiveClass("int"))
    assert intr.canApply(Integer, Number)
    assert !intr.canApply(Integer, String)
  }

  @Test
  void testCapitalized() {
    assertEquals "Name", intr.prefixed("name", "")
  }

  @Test
  void testProperty() {
    def prop = intr.property("name", Person)
    assertNotNull prop
    assertEquals "name", prop.name
    assertEquals String, prop.typeInfo.determineType()
    def p = new Person(name: "Person's Name")
    assertEquals p.name, prop.get(p)
    prop.set(p, "New Name")
    assertEquals "New Name", p.name
    prop = intr.property("sex", LegacyDetails)
    def ld = new LegacyDetails(sex: true)
    assertTrue prop.get(ld)
    prop = intr.property("children", Person)
    assertEquals Child, prop.typeInfo.determineType()
    assertEquals List, prop.typeInfo.type
    assert !intr.property("non-existent", Person)
  }

  @Test
  void testProperties() {
    assert intr.properties(Person).containsAll(["name", "lastName", "birthDate", "gender", "children", "strDate"])
  }
}
