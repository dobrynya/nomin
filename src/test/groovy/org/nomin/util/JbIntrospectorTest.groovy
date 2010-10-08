package org.nomin.util

import org.nomin.entity.Child
import org.nomin.entity.LegacyDetails
import org.nomin.entity.Person

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

  @org.junit.Test
  void testInvocation() {
    def invocation = intr.invocation("methodToFind", getClass(), "String", 1, null, 1L)
    assert invocation && invocation.invoke(this) == "String 1 null 1"
  }

  @org.junit.Test
  void testCanApply() {
    assert intr.canApply(null, String)
    assert intr.canApply(String, String)
    assert intr.canApply(Class.getPrimitiveClass("int"), Class.getPrimitiveClass("int"))
    assert intr.canApply(Integer, Number)
    assert !intr.canApply(Integer, String)
  }

  @org.junit.Test
  void testCapitalized() {
    assert "Name" == intr.prefixed("name", "")
  }

  @org.junit.Test
  void testProperty() {
    def prop = intr.property("name", Person)
    assert prop
    assert "name" == prop.name
    assert String == prop.typeInfo.determineType()
    def p = new Person(name: "Person's Name")
    assert p.name == prop.get(p)
    prop.set(p, "New Name")
    assert "New Name" == p.name
    prop = intr.property("sex", LegacyDetails)
    def ld = new LegacyDetails(sex: true)
    assert prop.get(ld)
    prop = intr.property("children", Person)
    assert Child == prop.typeInfo.determineType()
    assert List == prop.typeInfo.type
    assert !intr.property("non-existent", Person)
  }

  @org.junit.Test
  void testProperties() {
    assert intr.properties(Person).containsAll(["name", "lastName", "birthDate", "gender", "children", "strDate"])
  }
}
