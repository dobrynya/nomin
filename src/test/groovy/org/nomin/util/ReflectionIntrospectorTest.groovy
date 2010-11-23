package org.nomin.util

import org.nomin.entity.Child
import org.nomin.entity.LegacyDetails
import org.nomin.entity.Person
import org.nomin.core.MappingConsts

/**
 * Tests ReflectionIntrospector.
 * @author Dmitry Dobrynin
 * Created 14.04.2010 17:51:18
 */
@SuppressWarnings("GroovyVariableNotAssigned")
class ReflectionIntrospectorTest implements MappingConsts {
  String methodToFind(String s, int i, String s2, long l) {
    "${s} ${i} ${s2} ${l}"
  }

  @org.junit.Test
  void testInvocation() {
    def invocation = jb.invocation("methodToFind", getClass(), "String", 1, null, 1L)
    assert invocation && invocation.invoke(this) == "String 1 null 1"
  }

  @org.junit.Test
  void testCanApply() {
    assert jb.canApply(null, String)
    assert jb.canApply(String, String)
    assert jb.canApply(Class.getPrimitiveClass("int"), Class.getPrimitiveClass("int"))
    assert jb.canApply(Integer, Number)
    assert !jb.canApply(Integer, String)
  }

  @org.junit.Test
  void testProperty() {
    def prop = jb.property("name", Person)
    assert prop
    assert "name" == prop.name
    assert String == prop.typeInfo.type
    def p = new Person(name: "Person's Name")
    assert p.name == prop.get(p)
    prop.set(p, "New Name")
    assert "New Name" == p.name
    prop = jb.property("sex", LegacyDetails)
    def ld = new LegacyDetails(sex: true)
    assert prop.get(ld)
    prop = jb.property("children", Person)
    assert Child == prop.typeInfo.parameters[0].type
    assert List == prop.typeInfo.type
    assert !jb.property("non-existent", Person)
  }

  @org.junit.Test
  void testProperties() {
    assert jb.properties(Person).containsAll(["name", "lastName", "birthDate", "gender", "children", "strDate"])
  }
}
