package org.nomin.util

import org.nomin.entity.Person

/**
 * Document please.
 * @author Dmitry Dobrynin
 * Created 21.05.2010 13:15:35
 */
class ExplodingIntrospectorTest {
  ExplodingIntrospector intr = new ExplodingIntrospector()

  @org.junit.Test
  void testProperties() {
    assert ["name", "lastName", "birthDate", "gender", "children", "strDate", "options"].containsAll(intr.properties(Person))
  }
}
