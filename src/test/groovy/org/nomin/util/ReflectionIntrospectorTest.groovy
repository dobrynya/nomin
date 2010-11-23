package org.nomin.util

import org.nomin.entity.Child
import org.nomin.entity.LegacyDetails
import org.nomin.entity.Person

/**
 * Tests ReflectionIntrospector.
 * @author Dmitry Dobrynin
 * Created 14.04.2010 17:51:18
 */
class ReflectionIntrospectorTest {
  Introspector jb = new ReflectionIntrospector(JbNamingPolicy.jbNamingPolicy);

  @org.junit.Test
  void testProperty() {
    def pa = jb.property("a", this.class)
    assert pa && "a" == pa.name && String == pa.typeInfo.type && "a" == pa.get(this)
    pa.set(this, "new a")
    assert a == "new a"

    assert !jb.property("non-existent", this.class)
  }

  @org.junit.Test
  void testProperties() {
    assert jb.properties(Person).containsAll(["name", "lastName", "birthDate", "gender", "children", "strDate"])
  }

  String a = "a"
}
