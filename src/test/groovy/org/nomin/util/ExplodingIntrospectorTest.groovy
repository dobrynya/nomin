package org.nomin.util

import org.nomin.entity.Person

/**
 * Tests ExplodingIntrospector.
 * @author Dmitry Dobrynin
 * Created 21.05.2010 13:15:35
 */
class ExplodingIntrospectorTest {
  Introspector exploding = new ExplodingIntrospector();

  @org.junit.Test
  void testProperty() {
    def pa = exploding.property("a", this.class)
    assert pa && pa.get(this) == "a"
    pa.set(this, "newA")
    assert a == "newA"
  }

  @org.junit.Test
  void testProperties() {
    assert ["name", "lastName", "birthDate", "gender", "children", "strDate", "options"].containsAll(exploding.properties(Person))
  }

  private String a = "a"
}
