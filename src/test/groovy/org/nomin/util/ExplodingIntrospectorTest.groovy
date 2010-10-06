package org.nomin.util

import org.junit.Test
import org.nomin.entity.Person

/**
 * Document please.
 * @author Dmitry Dobrynin
 * Created 21.05.2010 13:15:35
 */
class ExplodingIntrospectorTest {
  ExplodingIntrospector intr = new ExplodingIntrospector()

  @Test
  void testProperties() {
    assert ["name", "lastName", "birthDate", "gender", "children", "strDate"].containsAll(intr.properties(Person))
  }
}
