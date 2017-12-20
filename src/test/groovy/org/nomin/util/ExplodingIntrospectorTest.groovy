package org.nomin.util

import org.junit.Test
import org.nomin.entity.Person

/**
 * Tests ExplodingIntrospector.
 * @author Dmitry Dobrynin
 * Created 21.05.2010 13:15:35
 */
class ExplodingIntrospectorTest {
  Introspector exploding = new ExplodingIntrospector();

  @Test
  void testProperty() {
    def pa = exploding.property("a", this.class)
    assert pa && pa.get(this) == "a"
    pa.set(this, "newA")
    assert a == "newA"
  }

  @Test
  void testProperties() {
    assert ["name", "lastName", "birthDate", "gender", "children", "strDate", "options"].containsAll(exploding.properties(Person))
  }

  private String a = "a"
}
